package com.demo.factory.repository.facility;

import com.demo.factory.domain.Facility;
import com.demo.factory.domain.QFacility;
import com.demo.factory.dto.facility.FacilityDtoForList;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class FacilityRepositoryImpl extends QuerydslRepositorySupport implements FacilityRepositoryCustom {

	public FacilityRepositoryImpl() {
		super(Facility.class);
	}

	@Override
	public Page<FacilityDtoForList> getSearchRestPage(Long siteNo, String type, String keyword, Pageable pageable) throws Exception {
		QFacility facility = QFacility.facility;
		JPQLQuery<Facility> query = from(facility);
		query.where(facility.factory.factoryNo.eq(siteNo));
		query.offset(pageable.getOffset());
		query.limit(pageable.getPageSize());
		query.orderBy(facility.facilityNo.desc());
		List<Facility> resultList = query.fetch();
		List<FacilityDtoForList> facilityDtoList = resultList.stream()
				.map(FacilityDtoForList::new)
				.toList();
		long total = query.fetchCount();
		return new PageImpl<>(facilityDtoList, pageable, total);
	}

}
