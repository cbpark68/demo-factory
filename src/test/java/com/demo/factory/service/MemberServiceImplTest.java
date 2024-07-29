package com.demo.factory.service;

import com.demo.factory.domain.Member;
import com.demo.factory.domain.MemberAuthEnum;
import com.demo.factory.dto.member.MemberDto;
import com.demo.factory.dto.factory.FactoryDtoForLogin;
import com.demo.factory.repository.member.MemberRepository;
import com.demo.factory.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@Sql(scripts = {"/data/insert.sql"},
        config = @SqlConfig(encoding = "utf-8", transactionMode = SqlConfig.TransactionMode.ISOLATED))
class MemberServiceImplTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("서비스 등록 테스트")
    void create() throws Exception {
        MemberDto memberDto = new MemberDto();
        memberDto.setUserId("testId");
        memberDto.setUserPw("1234");
        memberDto.setUserName("테스트");
        memberDto.setDefaultDashboardNo("999");
        memberDto.setFactory(new FactoryDtoForLogin(2L));
        Long userNo = memberService.restCreate(memberDto);
        Member regedMember = memberService.find(userNo);
        assertThat(regedMember.getUserId()).isEqualTo("testId");
        assertThat(regedMember.getUserPw()).isEqualTo("1234");
        assertThat(regedMember.getUserName()).isEqualTo("테스트");
        assertThat(regedMember.getDefaultDashboardNo()).isEqualTo("999");
        assertThat(regedMember.getAuthList().get(0).getAuth()).isEqualTo(MemberAuthEnum.ROLE_FACTORY_USER); //등록된 사용자는 반드시 팩토리사용자 권한을 가져야 한다.
        assertThat(regedMember.getFactory().getFactoryNo()).isEqualTo(2L);
    }

    @Test
    @DisplayName("서비스 수정 테스트")
    void modify() throws Exception {
        Member oldMember = memberRepository.findByUserId("kfactory").orElse(new Member());
        MemberDto memberDto = new MemberDto(oldMember);
        memberDto.setUserPw("12341234");
        memberDto.setUserName("테스트");
        memberDto.setDefaultDashboardNo("888");
        Long userNo = memberService.restModify(memberDto);
        Member findMember = memberService.find(userNo);
        assertThat(findMember.getUserId()).isEqualTo("kfactory");
        assertThat(findMember.getUserPw()).isEqualTo("12341234");
        assertThat(findMember.getUserName()).isEqualTo("테스트");
        assertThat(findMember.getDefaultDashboardNo()).isEqualTo("888");
        assertThat(findMember.getAuthList().size()).isEqualTo(1);
        assertThat(findMember.getAuthList().get(0).getAuth()).isEqualTo(MemberAuthEnum.ROLE_FACTORY_MANAGER);
        assertThat(findMember.getFactory().getFactoryNo()).isEqualTo(2L); //등록된 팩토리는 변경할수 없다.

    }


    @Test
    @DisplayName("서비스 팩토리 사용자 조회 테스트")
    void findFactoryUser() throws Exception {
        Member member = memberRepository.findByUserIdAndFactoryNo("sfactory", 3L).orElse(new Member());
        assertThat(member.getUserId()).isEqualTo("sfactory");

    }

    @Test
    @DisplayName("서비스 팩토리 관리자 조회 테스트")
    void findFactoryManager() throws Exception {
        Member factoryManager = memberService.findFactoryManager( 3L);
        assertThat(factoryManager.getUserId()).isEqualTo("sfactory");
    }

    @Test
    @DisplayName("서비스 삭제 테스트")
    void remove() throws Exception {
        memberService.removeByParent(2L,2L);
        Member member = memberService.find(2L);
        assertThat(member.getUserNo()).isNull();
    }

}