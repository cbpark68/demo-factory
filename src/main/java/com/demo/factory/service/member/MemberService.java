package com.demo.factory.service.member;

import com.demo.factory.domain.Member;
import com.demo.factory.dto.member.MemberDto;
import com.demo.factory.dto.member.MemberDtoForList;
import com.demo.factory.vo.PageRequestVO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MemberService {

	public Page<MemberDtoForList> restListV1(PageRequestVO pageRequestVO) throws Exception;

	public Long restCreate(MemberDto memberDto) throws Exception;

	public Long restModify(MemberDto memberDto) throws Exception;

	public List<MemberDto> formList() throws Exception;

	public Long formCreate(MemberDto memberDto) throws Exception;

	public Long formModify(MemberDto memberDto) throws Exception;

	public void setupAdmin(MemberDto memberDto) throws Exception;

	public Member find(Long userNo) throws Exception;

	public MemberDto findDto(Long userNo) throws Exception;

	public void remove(Long userNo) throws Exception;

	public void removeByParent(Long factoryNo, Long userNo) throws Exception;

	public MemberDto findDtoByUserId(String userId) throws Exception;

	public Member findFactoryManager(Long factoryNo) throws Exception;
}
