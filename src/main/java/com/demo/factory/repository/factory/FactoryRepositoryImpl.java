package com.demo.factory.repository.factory;

import com.demo.factory.domain.Factory;
import com.demo.factory.domain.QFactory;
import com.demo.factory.dto.factory.FactoryDtoForList;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class FactoryRepositoryImpl extends QuerydslRepositorySupport implements FactoryRepositoryCustom {

	public FactoryRepositoryImpl() {
		super(Factory.class);
	}

	@Override
	public Page<Factory> getSearchPage(String searchType, String keyword, Pageable pageable) {
		String siteName = keyword;

		QFactory factory = QFactory.factory;
		
		JPQLQuery<Factory> query = from(factory);
		
		if(searchType != null && !searchType.isEmpty()) {
			if(searchType.equals("t")) {
				query.where(factory.factoryName.like("%" + siteName +"%"));
				query.orderBy(factory.factoryNo.desc());
			}
			else {
				query.where(factory.factoryNo.gt(0L));
				query.orderBy(factory.factoryNo.desc());
			}
		}
		else {
			query.where(factory.factoryNo.gt(0L));
			query.orderBy(factory.factoryNo.desc());
		}
		
		query.offset(pageable.getOffset());
		query.limit(pageable.getPageSize());
		
		List<Factory> resultList = query.fetch();
		
		long total = query.fetchCount();

		return new PageImpl<>(resultList, pageable, total);
	}


    @Override
	public Page<FactoryDtoForList> getSearchRestPageV1(String searchType, String keyword, Pageable pageable) {
		String factoryName = keyword;

		QFactory factory = QFactory.factory;

		JPQLQuery<Factory> query = from(factory);

		query.orderBy(factory.factoryNo.desc());
		query.offset(pageable.getOffset());
		query.limit(pageable.getPageSize());

		List<Factory> resultList = query.fetch();

		List<FactoryDtoForList> siteDtoList = resultList.stream()
				.map(FactoryDtoForList::new)
				.toList();

		long total = query.fetchCount();

		return new PageImpl<>(siteDtoList, pageable, total);
	}

}
