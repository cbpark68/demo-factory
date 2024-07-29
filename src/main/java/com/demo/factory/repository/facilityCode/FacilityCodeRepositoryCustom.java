package com.demo.factory.repository.facilityCode;

import com.demo.factory.dto.facilityCode.FacilityCodeDtoForList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FacilityCodeRepositoryCustom {
	
	public Page<FacilityCodeDtoForList> getSearchRestPage(Long factoryNo, String type, String keyword, Pageable pageable) throws Exception;

}
