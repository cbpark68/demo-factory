package com.demo.factory.repository.facilityCode;

import com.demo.factory.domain.FacilityCode;
import com.demo.factory.domain.QFacilityCode;
import com.demo.factory.dto.facilityCode.FacilityCodeDtoForList;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class FacilityCodeRepositoryImpl extends QuerydslRepositorySupport implements FacilityCodeRepositoryCustom {

	public FacilityCodeRepositoryImpl() {
		super(FacilityCode.class);
	}

	@Override
	public Page<FacilityCodeDtoForList> getSearchRestPage(Long siteNo, String searchType, String keyword, Pageable pageable) throws Exception {
		QFacilityCode facilityCode = QFacilityCode.facilityCode1;
		JPQLQuery<FacilityCode> query = from(facilityCode);
		query.offset(pageable.getOffset());
		query.limit(pageable.getPageSize());
		query.where(facilityCode.factory.factoryNo.eq(siteNo));
		query.orderBy(facilityCode.facilityCodeNo.desc());
		List<FacilityCode> resultList = query.fetch();
		List<FacilityCodeDtoForList> facilityCodeDtoList = resultList.stream()
				.map(FacilityCodeDtoForList::new)
				.toList();
		long total = query.fetchCount();
		return new PageImpl<>(facilityCodeDtoList, pageable, total);
	}

}
