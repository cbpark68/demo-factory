package com.demo.factory.service.facility;

import com.demo.factory.domain.Facility;
import com.demo.factory.dto.facility.FacilityDto;
import com.demo.factory.dto.facility.FacilityDtoForList;
import com.demo.factory.vo.PageRequestVO;
import org.springframework.data.domain.Page;

public interface FacilityService {
    public Long create(FacilityDto facilityDto) throws Exception;

    public Facility find(Long facilityNo, Long factoryNo) throws Exception;

    public Long modify(FacilityDto facilityDto) throws Exception;

    public void removeByParent(Long factoryNo, Long facilityNo) throws Exception;

    public Page<FacilityDtoForList> list(PageRequestVO pageRequestVO) throws Exception;


}
