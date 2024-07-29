package com.demo.factory.service.facilityCode;

import com.demo.factory.domain.FacilityCode;
import com.demo.factory.dto.facilityCode.FacilityCodeDto;
import com.demo.factory.dto.facilityCode.FacilityCodeDtoForList;
import com.demo.factory.vo.PageRequestVO;
import org.springframework.data.domain.Page;

public interface FacilityCodeService {

    public Long create(FacilityCodeDto facilityCodeDto) throws Exception;

    public FacilityCode find(Long facilityCodeNo, Long factoryNo) throws Exception;

    public Long modify(FacilityCodeDto facilityCodeDto) throws Exception;

    public void remove(Long facilityCodeNo, Long factoryNo) throws Exception;

    public Page<FacilityCodeDtoForList> list(PageRequestVO pageRequestVO) throws Exception;

}
