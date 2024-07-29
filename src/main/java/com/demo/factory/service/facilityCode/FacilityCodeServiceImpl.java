package com.demo.factory.service.facilityCode;

import com.demo.factory.domain.FacilityCode;
import com.demo.factory.dto.facilityCode.FacilityCodeDto;
import com.demo.factory.dto.facilityCode.FacilityCodeDtoForList;
import com.demo.factory.repository.facility.FacilityRepository;
import com.demo.factory.repository.facilityCode.FacilityCodeRepository;
import com.demo.factory.vo.PageRequestVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class FacilityCodeServiceImpl implements FacilityCodeService{
    private final FacilityCodeRepository facilityCodeRepository;
    private final FacilityRepository facilityRepository;

    @Override
    public Long create(FacilityCodeDto facilityCodeDto) throws Exception {
        return facilityCodeRepository.save(new FacilityCode(facilityCodeDto)).getFacilityCodeNo();
    }

    @Override
    public FacilityCode find(Long facilityCodeNo, Long factoryNo) throws Exception {
        return  facilityCodeRepository.findByPkAndFactoryNo(facilityCodeNo, factoryNo).orElse(new FacilityCode());
    }

    @Override
    public Long modify(FacilityCodeDto facilityCodeDto) throws Exception {
        //변경감지로 save()호출 없음
        Long facilityCodeNo = Long.valueOf(facilityCodeDto.getFacilityCodeNo());
        Long factoryNo = Long.valueOf(facilityCodeDto.getFactoryNo());
        FacilityCode oldFacilityCode = facilityCodeRepository.findByPkAndFactoryNo(facilityCodeNo, factoryNo).orElse(new FacilityCode());
        FacilityCode newFacilityCode = new FacilityCode().modify(oldFacilityCode, facilityCodeDto);
        return newFacilityCode.getFacilityCodeNo();
    }

    @Override
    public void remove(Long facilityCodeNo,Long factoryNo) throws Exception {
        facilityRepository.deleteByFacilityCodeNo(facilityCodeNo);
        facilityCodeRepository.deleteByPkAndFactoryNo(facilityCodeNo, factoryNo);
    }

    @Override
    public Page<FacilityCodeDtoForList> list(PageRequestVO pageRequestVO) throws Exception {
        String searchType = pageRequestVO.getSearchType();
        String keyword = pageRequestVO.getKeyword();
        Long factoryNo = pageRequestVO.getFactoryNo();

        int pageNumber = pageRequestVO.getPage() - 1;
        int sizePerPage = pageRequestVO.getSize();

        Pageable pageRequest = PageRequest.of(pageNumber, sizePerPage, Sort.Direction.DESC, "facilityCodeNo");

        return facilityCodeRepository.getSearchRestPage(factoryNo, searchType, keyword, pageRequest);
    }

}
