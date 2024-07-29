package com.demo.factory.repository.member;

import com.demo.factory.domain.Member;
import com.demo.factory.domain.QMember;
import com.demo.factory.dto.member.MemberDtoForList;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class MemberRepositoryImpl extends QuerydslRepositorySupport implements MemberRepositoryCustom {

	public MemberRepositoryImpl() {
		super(Member.class);
	}

	@Override
	public Page<MemberDtoForList> getSearchRestPageV1(Long factoryNo, String type, String keyword, Pageable pageable) {
		QMember member = QMember.member;

		JPQLQuery<Member> query = from(member);

		query.where(member.factory.factoryNo.eq(factoryNo));
		query.orderBy(member.userNo.desc());

		query.offset(pageable.getOffset());
		query.limit(pageable.getPageSize());

		List<Member> resultList = query.fetch();

		List<MemberDtoForList> memberDtoList = resultList.stream()
				.map(MemberDtoForList::new)
				.toList();

		long total = query.fetchCount();

		return new PageImpl<>(memberDtoList, pageable, total);
	}

}
