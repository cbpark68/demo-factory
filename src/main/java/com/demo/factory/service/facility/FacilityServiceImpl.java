package com.demo.factory.service.facility;

import com.demo.factory.domain.Facility;
import com.demo.factory.dto.facility.FacilityDto;
import com.demo.factory.dto.facility.FacilityDtoForList;
import com.demo.factory.repository.facility.FacilityRepository;
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
public class FacilityServiceImpl implements FacilityService{
    private final FacilityRepository facilityRepository;

    @Override
    public Long create(FacilityDto facilityDto) throws Exception {
        return facilityRepository.save(new Facility(facilityDto)).getFacilityNo();
    }

    @Override
    public Facility find(Long facilityNo,Long factoryNo) throws Exception {
        return facilityRepository.findByPkAndFactoryNo(facilityNo, factoryNo).orElse(new Facility());
    }

    @Override
    public Long modify(FacilityDto facilityDto) throws Exception {
        //변경감지로 save()호출 없음
        Long facilityNo = Long.valueOf(facilityDto.getFacilityNo());
        Long factoryNo = Long.valueOf(facilityDto.getFactoryNo());
        Long facilityCodeNo = Long.valueOf(facilityDto.getFacilityCodeNo());
        Facility oldFacility = facilityRepository.findByParent(factoryNo, facilityCodeNo, facilityNo).orElse(new Facility());
        Facility newFacility = new Facility().modify(oldFacility, facilityDto);
        return newFacility.getFacilityNo();
    }

    @Override
    public void removeByParent(Long factoryNo, Long facilityNo) throws Exception {
        facilityRepository.deleteByParent(factoryNo,facilityNo);
    }

    @Override
    public Page<FacilityDtoForList> list(PageRequestVO pageRequestVO) throws Exception {
        String searchType = pageRequestVO.getSearchType();
        String keyword = pageRequestVO.getKeyword();
        Long siteNo = pageRequestVO.getFactoryNo();

        int pageNumber = pageRequestVO.getPage() - 1;
        int sizePerPage = pageRequestVO.getSize();

        Pageable pageRequest = PageRequest.of(pageNumber, sizePerPage, Sort.Direction.DESC, "facilityNo");

        return facilityRepository.getSearchRestPage(siteNo, searchType, keyword, pageRequest);
    }

}
