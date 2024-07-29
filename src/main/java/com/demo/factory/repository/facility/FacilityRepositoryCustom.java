package com.demo.factory.repository.facility;

import com.demo.factory.dto.facility.FacilityDtoForList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FacilityRepositoryCustom {
	
	public Page<FacilityDtoForList> getSearchRestPage(Long siteNo, String type, String keyword, Pageable pageable) throws Exception;

}
