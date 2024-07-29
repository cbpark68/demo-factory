package com.demo.factory.service;

import com.demo.factory.domain.Factory;
import com.demo.factory.domain.FactoryStatusEnum;
import com.demo.factory.domain.Member;
import com.demo.factory.domain.MemberAuthEnum;
import com.demo.factory.dto.factory.FactoryDtoForManager;
import com.demo.factory.repository.member.MemberRepository;
import com.demo.factory.service.member.MemberService;
import com.demo.factory.service.factory.FactoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@Sql(scripts = {"/data/insert.sql"},
        config = @SqlConfig(encoding = "utf-8", transactionMode = SqlConfig.TransactionMode.ISOLATED))
class FactoryServiceImplTest {
    @Autowired
    private FactoryService factoryService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository; //팩토리번호로 회원조회시 필요

    @Test
    @DisplayName("서비스 삭제 테스트")
    void remove() throws Exception {
        factoryService.remove(2L);
        Factory factory = factoryService.find(2L);
        assertThat(factory.getFactoryNo()).isNull();
        List<Member> memberByList = memberRepository.findByFactoryNo(2L);
        assertThat(memberByList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("서비스 등록 테스트")
    void create() throws Exception {
        FactoryDtoForManager factoryDtoForManager = new FactoryDtoForManager();
        factoryDtoForManager.setFactoryName("테스트팩토리");
        factoryDtoForManager.setFactoryStatus(FactoryStatusEnum.STANDBY);
        factoryDtoForManager.setUserId("testFactoryManager");
        factoryDtoForManager.setUserPw("1234");
        factoryDtoForManager.setUserName("테스트매니저");

        Long[] results = factoryService.restCreateV1(factoryDtoForManager);
        Long factoryNo = results[0];
        Long userNo = results[1];

        Factory savedFactory = factoryService.find(factoryNo);
        assertThat(savedFactory.getFactoryName()).isEqualTo("테스트팩토리");
        assertThat(savedFactory.getFactoryStatus()).isEqualTo(FactoryStatusEnum.STANDBY);

        Member member = memberRepository.findByUserId("testFactoryManager").orElse(new Member());
        assertThat(member.getUserName()).isEqualTo("테스트매니저");
        assertThat(member.getAuthList().get(0).getAuth()).isEqualTo(MemberAuthEnum.ROLE_FACTORY_MANAGER); //등록된 사용자는 반드시 팩토리관리자 권한을 가져야 한다.
    }
    @Test
    @DisplayName("서비스 수정 테스트")
    void modify() throws Exception {
        Factory factory = factoryService.find(2L);
        Member member = memberService.find(2L);
        FactoryDtoForManager factoryDtoForManager = new FactoryDtoForManager(factory, member);
        factoryDtoForManager.setFactoryName("변경됨");
        factoryDtoForManager.setFactoryStatus(FactoryStatusEnum.ACTIVE); //팩토리상태는 변경되면 안된다.
        factoryDtoForManager.setUserPw("12341234");
        factoryDtoForManager.setUserName("변경됨");

        Long[] results = factoryService.restModifyV1(factoryDtoForManager);
        Long factoryNo = results[0];
        Long userNo = results[1];

        Factory findFactory = factoryService.find(factoryNo);
        assertThat(findFactory.getFactoryName()).isEqualTo("변경됨");
        assertThat(findFactory.getFactoryStatus()).isEqualTo(FactoryStatusEnum.ACTIVE);

        Member member2 = memberRepository.findByUserId("kfactory").orElse(new Member());
        assertThat(member2.getUserName()).isEqualTo("변경됨");
        assertThat(member2.getUserPw()).isEqualTo("12341234");
        assertThat(member2.getAuthList().get(0).getAuth()).isEqualTo(MemberAuthEnum.ROLE_FACTORY_MANAGER);
    }

    @Test
    @DisplayName("서비스 조회 테스트")
    void find() throws Exception {
        Factory factory = factoryService.find(2L);
        List<Member> memberList = memberRepository.findByFactoryNo(factory.getFactoryNo());
        assertThat(memberList.size()).isEqualTo(2);
    }

}