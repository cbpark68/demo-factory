package com.demo.factory.repository.member;

import com.demo.factory.dto.member.MemberDtoForList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
	
	public Page<MemberDtoForList> getSearchRestPageV1(Long siteNo, String type, String keyword, Pageable pageable);

}
