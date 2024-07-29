package com.demo.factory.controller.api;

import com.demo.factory.domain.Member;
import com.demo.factory.dto.PaginationDto;
import com.demo.factory.dto.member.MemberDto;
import com.demo.factory.dto.member.MemberDtoForList;
import com.demo.factory.dto.factory.FactoryDtoForLogin;
import com.demo.factory.repository.member.MemberRepository;
import com.demo.factory.service.member.MemberService;
import com.demo.factory.vo.PageRequestVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiMemberController {
    private final MemberService memberService;

    private final MemberRepository memberRepository;

    private final String urlV2 = "/v2/factory/{factoryNo}/user";
    private final String urlV1 = "/v1/factory/{factoryNo}/user";
    private final String urlV1Login = "/v1/user/{userId}/login";

    public record Result(String userNo) {
    }

    @GetMapping(value = urlV1+"/list")
    public PaginationDto<MemberDtoForList> list(@PathVariable("factoryNo") Long factoryNo, @ModelAttribute("pgrq") PageRequestVO pageRequestVO) throws Exception {
        pageRequestVO.setFactoryNo(factoryNo);
        Page<MemberDtoForList> page = memberService.restListV1(pageRequestVO);
        return new PaginationDto<>(page);
    }

    @GetMapping(value = urlV1Login)
    public MemberDto loginInfo(@PathVariable("userId") String userId) throws Exception {
        return memberService.findDtoByUserId(userId);
    }

    @PostMapping(value = urlV2)
    public Result create(@PathVariable("factoryNo") Long factoryNo,@RequestBody MemberDto memberDto) throws Exception {
        memberDto.setFactory(new FactoryDtoForLogin(factoryNo));
        Long userNo = memberService.restCreate(memberDto);
        return new Result(String.valueOf(userNo));
    }

    @PutMapping(value = urlV2)
    public Result modify(@PathVariable("factoryNo") Long factoryNo,@RequestBody MemberDto memberDto) throws Exception {
        memberDto.setFactory(new FactoryDtoForLogin(factoryNo));
        Long userNo = memberService.restModify(memberDto);
        return new Result(String.valueOf(userNo));
    }

    @DeleteMapping(value = urlV1+"/{userId}")
    public void remove(@PathVariable("factoryNo") Long factoryNo, @PathVariable("userId") String userId) throws Exception {
        Member member = memberRepository.findByUserIdAndFactoryNo(userId,factoryNo).orElse(new Member());
        memberService.removeByParent(factoryNo, member.getUserNo());
    }

}
