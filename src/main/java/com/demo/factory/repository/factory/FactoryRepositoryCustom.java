package com.demo.factory.repository.factory;

import com.demo.factory.domain.Factory;
import com.demo.factory.dto.factory.FactoryDtoForList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FactoryRepositoryCustom {
	
	public Page<Factory> getSearchPage(String type, String keyword, Pageable pageable);
	public Page<FactoryDtoForList> getSearchRestPageV1(String type, String keyword, Pageable pageable);

}
