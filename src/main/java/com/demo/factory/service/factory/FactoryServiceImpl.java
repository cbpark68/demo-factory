package com.demo.factory.service.factory;

import com.demo.factory.domain.FacilityCode;
import com.demo.factory.domain.Factory;
import com.demo.factory.domain.Member;
import com.demo.factory.dto.member.MemberDto;
import com.demo.factory.dto.factory.FactoryDto;
import com.demo.factory.dto.factory.FactoryDtoForList;
import com.demo.factory.dto.factory.FactoryDtoForManager;
import com.demo.factory.repository.facility.FacilityRepository;
import com.demo.factory.repository.facilityCode.FacilityCodeRepository;
import com.demo.factory.repository.member.MemberRepository;
import com.demo.factory.repository.factory.FactoryRepository;
import com.demo.factory.service.member.MemberService;
import com.demo.factory.vo.PageRequestVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class FactoryServiceImpl implements FactoryService {
    private final FactoryRepository factoryRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final FacilityCodeRepository facilityCodeRepository;
    private final FacilityRepository facilityRepository;

    @Override
    public Long formCreate(FactoryDto factoryDto) throws Exception {
        Factory factory = factoryRepository.save(new Factory(factoryDto));
        return factory.getFactoryNo();
    }

    @Override
    public Long[] restCreateV1(FactoryDtoForManager factoryDtoForManager) throws Exception {
        Factory factory = new Factory(factoryDtoForManager);
        Factory savedFactory = factoryRepository.save(factory);
        factoryDtoForManager.setFactoryNo(String.valueOf(savedFactory.getFactoryNo()));

        Member member = new Member(factoryDtoForManager);
        Member savedMember = memberRepository.save(member);
        return new Long[]{savedFactory.getFactoryNo(), savedMember.getUserNo()};
    }

    @Override
    public Factory find(Long factoryNo) throws Exception {
        return factoryRepository.findByPk(factoryNo).orElse(new Factory());
    }

    @Override
    public FactoryDto findDto(Long factoryNo) throws Exception {
        return new FactoryDto(factoryRepository.findByPk(factoryNo).orElse(new Factory()));
    }

    @Override
    public Long formModify(FactoryDto factoryDto) throws Exception {
        //변경감지로 save()호출 없음
        //폼에서 db업데이트는 상태만 가능
        Factory oldSite = factoryRepository.findByPk(Long.valueOf(factoryDto.getFactoryNo())).orElse(new Factory());
        Factory newSite = new Factory().modify(oldSite, factoryDto);
        return newSite.getFactoryNo();
    }

    @Override
    public Long[] restModifyV1(FactoryDtoForManager factoryDtoForManager) throws Exception {
        //변경감지로 save()호출 없음
        Factory oldSite = factoryRepository.findByPk(Long.valueOf(factoryDtoForManager.getFactoryNo())).orElse(new Factory());
        Factory newSite = new Factory().modify(oldSite, factoryDtoForManager);
        Member oldMember = memberRepository.findByUserIdAndFactoryNo(factoryDtoForManager.getUserId(), Long.valueOf(factoryDtoForManager.getFactoryNo())).orElse(new Member());
        Member newMember = new Member().modify(oldMember, new MemberDto(factoryDtoForManager));
        return new Long[]{newSite.getFactoryNo(), newMember.getUserNo()};
    }

    @Override
    public void remove(Long factoryNo) throws Exception {
        removeFacilityCode(factoryNo);
        removeFacility(factoryNo);
        factoryRepository.deleteById(factoryNo);
    }

    @Override
    public List<FactoryDto> formList() throws Exception {
        List<Factory> list = factoryRepository.findAll(Sort.by(Sort.Direction.DESC, "factoryNo"));
        return list.stream().map(s-> {
            try {
                return new FactoryDto(s);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public Page<FactoryDtoForList> restListV1(PageRequestVO pageRequestVO) throws Exception {
        String searchType = pageRequestVO.getSearchType();
        String keyword = pageRequestVO.getKeyword();

        int pageNumber = pageRequestVO.getPage() - 1;
        int sizePerPage = pageRequestVO.getSize();

        Pageable pageRequest = PageRequest.of(pageNumber, sizePerPage, Sort.Direction.DESC, "factoryNo");

        Page<FactoryDtoForList> siteDtoForList = factoryRepository.getSearchRestPageV1(searchType, keyword, pageRequest);
        siteDtoForList.getContent().forEach(content -> {
            try {
                content.setFactoryManager(memberService.findFactoryManager(Long.valueOf(content.getFactoryNo())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return siteDtoForList;
    }

    @Override
    public String getLogoFileName(Long factoryNo) {
        Factory site = factoryRepository.findByPk(factoryNo).orElse(new Factory());
        return site.getLogoFileName();
    }


    private void removeFacility(Long siteNo) {
        facilityRepository.deleteByFactoryNo(siteNo);
    }

    private void removeFacilityCode(Long siteNo) {
        List<FacilityCode> facilityCodeList = facilityCodeRepository.findByFactoryNo(siteNo);
        for (FacilityCode facilityCode : facilityCodeList) {
            facilityRepository.deleteByFacilityCodeNo(facilityCode.getFacilityCodeNo());
        }
        facilityCodeRepository.deleteByFactoryNo(siteNo);
    }


}
