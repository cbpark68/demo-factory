package com.demo.factory.service.member;

import com.demo.factory.domain.Member;
import com.demo.factory.domain.MemberAuth;
import com.demo.factory.domain.MemberAuthEnum;
import com.demo.factory.domain.Factory;
import com.demo.factory.dto.member.MemberDto;
import com.demo.factory.dto.member.MemberDtoForList;
import com.demo.factory.dto.factory.FactoryDtoForLogin;
import com.demo.factory.repository.member.MemberRepository;
import com.demo.factory.repository.factory.FactoryRepository;
import com.demo.factory.vo.PageRequestVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final FactoryRepository factoryRepository;

	@Override
	public void setupAdmin(MemberDto memberDto) throws Exception {
		Member member = new Member(memberDto);
		member.setMemberAuth(new MemberAuth(MemberAuthEnum.ROLE_ADMIN));
		memberRepository.save(member);
	}

	@Override
	public Long formCreate(MemberDto memberDto) throws Exception {
		Member member = memberRepository.save(new Member(memberDto));
		return member.getUserNo();
	}

	@Override
	public Long restCreate(MemberDto memberDto) {
		return memberRepository.save(new Member(memberDto)).getUserNo();
	}

	@Override
	public Member find(Long userNo) throws Exception {
		return memberRepository.findByPk(userNo).orElse(new Member());
	}

	@Override
	public MemberDto findDto(Long userNo) throws Exception {
		return new MemberDto(memberRepository.findByPk(userNo).orElse(new Member()));
	}

	@Override
	public Long formModify(MemberDto memberDto) throws Exception {
		//변경감지로 save()호출 없음
		Member oldMember = memberRepository.findByPk(Long.valueOf(memberDto.getUserNo())).orElse(new Member());
        Member newMember = new Member().modify(oldMember, memberDto);
		return newMember.getUserNo();
	}

	@Override
	public Long restModify(MemberDto memberDto) throws Exception {
		//변경감지로 save()호출 없음
		Member oldMember  = memberRepository.findByUserIdAndSiteNo(memberDto.getUserId(), Long.valueOf(memberDto.getFactory().getFactoryNo())).orElse(new Member());
		Member newMember = new Member().modify(oldMember, memberDto);
		return newMember.getUserNo();
	}

	@Override
	public void remove(Long userNo) throws Exception {
		memberRepository.deleteById(userNo);
	}

	@Override
	public void removeByParent(Long factoryNo, Long userNo) throws Exception {
		memberRepository.removeAuthByUserNo(userNo);
		memberRepository.removeByParent(factoryNo,userNo);
	}

	@Override
	public List<MemberDto> formList() throws Exception {
		List<Member> list = memberRepository.findAll(Sort.by(Sort.Direction.DESC, "userNo"));
		return list.stream().map(MemberDto::new).toList();
	}

	@Override
	public Page<MemberDtoForList> restListV1(PageRequestVO pageRequestVO) throws Exception {
		Long siteNo = pageRequestVO.getFactoryNo();
		String searchType = pageRequestVO.getSearchType();
		String keyword = pageRequestVO.getKeyword();

		int pageNumber = pageRequestVO.getPage() - 1;
		int sizePerPage = pageRequestVO.getSize();

		Pageable pageRequest = PageRequest.of(pageNumber, sizePerPage, Sort.Direction.DESC, "factoryNo");

		return memberRepository.getSearchRestPageV1(siteNo,searchType, keyword, pageRequest);
	}

	@Override
	public MemberDto findDtoByUserId(String userId) throws Exception {
		Member member = memberRepository.findByUserId(userId).orElse(new Member());
		Factory site = factoryRepository.findByPk(member.getFactory().getFactoryNo()).orElse(new Factory());
		FactoryDtoForLogin siteDtoForLogin = new FactoryDtoForLogin(site);
		return new MemberDto(member,siteDtoForLogin);
	}

	@Override
	public Member findFactoryManager(Long factoryNo) {
		List<Member> memberList = memberRepository.findBySiteNo(factoryNo);
		for (Member member : memberList) {
			if (member.getUserNo() != null) {
				if (member.getAuthList().get(0).getAuth().equals(MemberAuthEnum.ROLE_SITE_MANAGER)) {
					return member;
				}
			}
		}
		return new Member();
	}

}
