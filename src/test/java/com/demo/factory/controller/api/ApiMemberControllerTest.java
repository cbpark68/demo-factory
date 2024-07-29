package com.demo.factory.controller.api;

import com.demo.factory.common.exception.ErrorResult;
import com.demo.factory.domain.Member;
import com.demo.factory.domain.MemberAuthEnum;
import com.demo.factory.dto.member.MemberDto;
import com.demo.factory.dto.member.MemberDtoForList;
import com.demo.factory.repository.member.MemberRepository;
import com.demo.factory.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@Sql(scripts = {"/data/insert.sql"},
        config = @SqlConfig(encoding = "utf-8", transactionMode = SqlConfig.TransactionMode.ISOLATED))
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class ApiMemberControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    public record Result(String userNo) {
    }

    @BeforeEach
    void setUpForMockmvc(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 리스트 테스트")
    void listByMockMvc() throws Exception {
        String url = "http://localhost:" + port + "/api/v1/factory/{factoryNo}/user/list?page=1&size=10";
//        String strResult = restTemplate.getForObject(url, String.class);
        String contentAsString = mockMvc.perform(RestDocumentationRequestBuilders.get(url,"2")) //method: list
                .andExpect(status().isOk())
                .andDo(document("/member/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("factoryNo").description("팩토리번호")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지번호"),
                                parameterWithName("size").description("페이지당 최대 데이터건수")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("page").description("페이지객체"),
                                        fieldWithPath("page.content[]").description("페이지데이터배열"),
                                        fieldWithPath("page.content[].userNo").description("사용자번호"),
                                        fieldWithPath("page.content[].userId").description("사용자아이디"),
                                        fieldWithPath("page.content[].userPw").description("사용자비번"),
                                        fieldWithPath("page.content[].userName").description("사용자이름"),
                                        fieldWithPath("page.content[].auth").description("사용자권한: ROLE_FACTORY_USER/ROLE_FACTORY_MANAGER/ROLE_ADMIN"),
                                        fieldWithPath("page.content[].defaultDashboardNo").description("디폴트대시보드번호"),
                                        fieldWithPath("page.content[].createDatetime").description("최초등록일시"),
                                        fieldWithPath("page.content[].lastModifyDatetime").description("최종수정일시"),
                                        fieldWithPath("page.empty").description("페이지데이터 없음"),
                                        fieldWithPath("page.first").description("첫페이지"),
                                        fieldWithPath("page.last").description("마지막페이지"),
                                        fieldWithPath("page.totalPages").description("전체페이지수"),
                                        fieldWithPath("page.totalElements").description("전체데이터건수"),
                                        fieldWithPath("page.size").description("페이지당 최대 데이터건수"),
                                        fieldWithPath("page.number").description("현재페이지번호(-1)"),
                                        fieldWithPath("page.numberOfElements").description("현재페이지에 출력한 데이터건수"),
                                        fieldWithPath("page.pageable").description("페이지정보객체"),
                                        fieldWithPath("page.pageable.pageNumber").description("현재페이지번호(-1)"),
                                        fieldWithPath("page.pageable.pageSize").description("페이지당 최대 데이터건수"),
                                        fieldWithPath("page.pageable.sort").description("정렬객체"),
                                        fieldWithPath("page.pageable.sort.empty").description("없음"),
                                        fieldWithPath("page.pageable.sort.sorted").description("정렬"),
                                        fieldWithPath("page.pageable.sort.unsorted").description("정렬안함"),
                                        fieldWithPath("page.pageable.offset").description("데이터 옵셋번호"),
                                        fieldWithPath("page.pageable.paged").description("페이징됨"),
                                        fieldWithPath("page.pageable.unpaged").description("페이징되지 않음"),
                                        fieldWithPath("page.sort").description("정렬객체"),
                                        fieldWithPath("page.sort.empty").description("없음"),
                                        fieldWithPath("page.sort.sorted").description("정렬"),
                                        fieldWithPath("page.sort.unsorted").description("정렬안함")
                                )
                        ))
                ).andReturn().getResponse().getContentAsString();
        MemberDtoForList memberDto = objectMapper.treeToValue(objectMapper.readTree(contentAsString).get("page").get("content").get(0), MemberDtoForList.class);
        assertThat(memberDto.getUserNo()).isEqualTo("4");
        assertThat(memberDto.getUserId()).isEqualTo("kfactory1");
        assertThat(memberDto.getUserPw()).isEqualTo("admin");
        assertThat(memberDto.getAuth()).isIn(MemberAuthEnum.ROLE_FACTORY_USER);
    }


    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 로그인 테스트")
    void loginByMockMvc() throws Exception {
        String url = "http://localhost:" + port + "/api/v1/user/{userId}/login";
//        MemberDto memberDto = restTemplate.getForObject(url, MemberDto.class);
        String contentAsString = mockMvc.perform(RestDocumentationRequestBuilders.get(url,"admin" )) //method: login
                .andExpect(status().isOk())
                .andDo(document("member/login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("사용자아이디: 로그인대상")
                        ),
                        responseFields(
                                fieldWithPath("userNo").description("사용자번호"),
                                fieldWithPath("userId").description("사용자아이디"),
                                fieldWithPath("userPw").description("사용자비번"),
                                fieldWithPath("userName").description("사용자이름"),
                                fieldWithPath("auth").description("사용자권한"),
                                fieldWithPath("defaultDashboardNo").description("디폴트대시보드번호"),
                                fieldWithPath("factory").description("팩토리객체"),
                                fieldWithPath("factory.factoryNo").description("팩토리번호"),
                                fieldWithPath("factory.factoryName").description("팩토리이름"),
                                fieldWithPath("factory.factoryStatus").description("팩토리상태"),
                                fieldWithPath("factory.logoFileName").description("로그인파일이름"),
                                fieldWithPath("factory.createDatetime").description("최초등록일시"),
                                fieldWithPath("factory.lastModifyDatetime").description("최종수정일시"),
                                fieldWithPath("createDatetime").description("최초등록일시"),
                                fieldWithPath("lastModifyDatetime").description("최종수정일시")
                        )
                ))
                .andReturn().getResponse().getContentAsString();

        MemberDto memberDto = objectMapper.readValue(contentAsString, MemberDto.class);
        assertThat(memberDto.getUserNo()).isEqualTo("1");
        assertThat(memberDto.getUserId()).isEqualTo("admin");
        assertThat(memberDto.getUserPw()).isEqualTo("1234");
        assertThat(memberDto.getAuth()).isEqualTo(MemberAuthEnum.ROLE_ADMIN);
        assertThat(memberDto.getFactory().getFactoryNo()).isEqualTo("1");
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 등록 테스트")
    void createByMockMvc() throws Exception {
        MemberDto memberDto = new MemberDto();
        memberDto.setUserNo("2"); //사용자번호는 무시되어야 한다.
        memberDto.setUserId("testId");
        memberDto.setUserPw("1234");
        memberDto.setUserName("테스트유저");
        memberDto.setDefaultDashboardNo("999");
        memberDto.setAuth(MemberAuthEnum.ROLE_ADMIN);
        String dtoAsString = objectMapper.writeValueAsString(memberDto);

//        ResponseEntity<String> response = restTemplate.postForEntity(url, memberDto, String.class);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String url = "http://localhost:" + port + "/api/v2/factory/{factoryNo}/user";
        String contentAsString = mockMvc.perform(RestDocumentationRequestBuilders.post(url, "2") //method: create
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoAsString))
                .andDo(document("member/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("factoryNo").description("팩토리번호: 사용자를 관리할 팩토리")
                        ),
                        requestFields(
                                fieldWithPath("userNo").description("사용자번호: 자동생성"),
                                fieldWithPath("userId").description("사용자아이디"),
                                fieldWithPath("userPw").description("사용자비번"),
                                fieldWithPath("userName").description("사용자이름"),
                                fieldWithPath("defaultDashboardNo").description("디폴트대시보드번호"),
                                fieldWithPath("auth").description("사용자권한: N/A"),
                                fieldWithPath("factory").description("팩토리객체: N/A"),
                                fieldWithPath("createDatetime").description("최초등록일시: 자동생성"),
                                fieldWithPath("lastModifyDatetime").description("최종수정일시: 자동생성")
                        ),
                        responseFields(
                                fieldWithPath("userNo").description("사용자번호: 자동생성")
                        )
                ))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        Result result = objectMapper.readValue(contentAsString, Result.class);
        Member member = memberService.find(Long.valueOf(result.userNo()));
        assertThat(member.getUserNo()).isNotEqualTo(2L);
        assertThat(member.getUserId()).isEqualTo("testId");
        assertThat(member.getUserName()).isEqualTo("테스트유저");
        assertThat(member.getUserPw()).isEqualTo("1234");
        assertThat(member.getDefaultDashboardNo()).isEqualTo("999");
        assertThat(member.getAuthList().get(0).getAuth()).isEqualTo(MemberAuthEnum.ROLE_FACTORY_USER); //등록된 사용자권한은 반드시 팩토리사용자권한 이어야 함.
    }



    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 수정 테스트")
    void modifyByMockMvc() throws Exception {
        MemberDto memberDto = new MemberDto();
        memberDto.setUserId("kfactory");
        memberDto.setUserPw("12341234");
        memberDto.setUserName("테스트유저");
        memberDto.setDefaultDashboardNo("999");
        memberDto.setAuth(MemberAuthEnum.ROLE_ADMIN); //권한은 변경되지 않아야 한다.
        String dtoAsString = objectMapper.writeValueAsString(memberDto);

//        restTemplate.put(url, memberDto);
        String url = "http://localhost:" + port + "/api/v2/factory/{factoryNo}/user";
        String contentAsString = mockMvc.perform(RestDocumentationRequestBuilders.put(url, "2") //method: modify
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoAsString))
                .andDo(document("member/modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("factoryNo").description("팩토리번호: 사용자를 관리할 팩토리")
                        ),
                        requestFields(
                                fieldWithPath("userNo").description("사용자번호: N/A"),
                                fieldWithPath("userId").description("사용자아이디: 수정대상"),
                                fieldWithPath("userPw").description("사용자비번"),
                                fieldWithPath("userName").description("사용자이름"),
                                fieldWithPath("defaultDashboardNo").description("디폴트대시보드번호"),
                                fieldWithPath("auth").description("사용자권한: N/A"),
                                fieldWithPath("factory").description("팩토리객체: N/A"),
                                fieldWithPath("createDatetime").description("최초등록일시: 자동생성"),
                                fieldWithPath("lastModifyDatetime").description("최종수정일시: 자동생성")
                        ),
                        responseFields(
                                fieldWithPath("userNo").description("사용자번호: 자동생성")
                        )
                ))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        Result result = objectMapper.readValue(contentAsString, Result.class);
        Member member = memberService.find(Long.valueOf(result.userNo));
        assertThat(member.getUserPw()).isEqualTo("12341234");
        assertThat(member.getUserName()).isEqualTo("테스트유저");
        assertThat(member.getDefaultDashboardNo()).isEqualTo("999");
        assertThat(member.getFactory().getFactoryNo()).isEqualTo(2);
        assertThat(member.getAuthList().get(0).getAuth()).isEqualTo(MemberAuthEnum.ROLE_FACTORY_MANAGER); //사용자권한은 변경되지 않아야 한다.
    }


    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 삭제 테스트")
    void removeByMockMvc() throws Exception {
        String url = "http://localhost:" + port + "/api/v1/factory/{factoryNo}/user/{userId}";
//        restTemplate.delete(url);

        mockMvc.perform(RestDocumentationRequestBuilders.delete(url,"1","admin")) //method: remove
                .andExpect(status().is2xxSuccessful())
                .andDo(document("/member/remove",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("factoryNo").description("팩토리번호: 삭제대상"),
                                        parameterWithName("userId").description("사용자아이디: 삭제대상")
                                )
                        )
                )
                .andReturn().getResponse().getContentAsString();

        Member member = memberRepository.findByUserId("admin").orElse(new Member());
        assertThat(member.getUserId()).isNull();
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 사용자가 있지만 해당팩토리에 없는 사용자 삭제 테스트")
    void removeForWrongFactory() throws Exception {
        String url = "http://localhost:" + port + "/api/v1/factory/2/user/admin";
        mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(status().isOk());
        //삭제를 실패했기때문에 사용자가 조회되어야 한다.
        Member member = memberRepository.findByUserId("admin").orElse(new Member());
        assertThat(member.getUserId()).isEqualTo("admin");
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 처음부터 존재하지 않는 사용자 삭제 테스트")
    void removeForNotExits() throws Exception {
        String url = "http://localhost:" + port + "/api/v1/factory/1/user/notExistUser";
        mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(status().isOk());
        //존재하지 않는 사용자이므로 조회되면 안된다.
        Member member = memberRepository.findByUserId("notExistUser").orElse(new Member());
        assertThat(member.getUserId()).isNull();
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 존재하지 않은 사용자아이디 로그인 테스트")
    void loginByMockMvcWithWrongUserId() throws Exception {
        String url = "http://localhost:" + port + "/api/v1/user/{userId}/login";
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get(url,"noExist" )) //method: login
                .andExpect(status().is5xxServerError())
                .andReturn().getResponse().getContentAsString();

        log.info("errorResult = "+contentAsString);
        ErrorResult errorResult = objectMapper.readValue(contentAsString, ErrorResult.class);
        assertThat(errorResult.getCode()).isEqualTo("ERROR");
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 사용자등록시 주요항목 없는 등록 테스트")
    void createByMockMvcWithNoData() throws Exception {
        MemberDto memberDto = new MemberDto();
        memberDto.setUserNo("999"); //사용자번호는 무시되어야 한다.
        memberDto.setUserId("testId");
        memberDto.setUserPw(null);
        memberDto.setUserName(null);
        memberDto.setDefaultDashboardNo(null);
        memberDto.setAuth(null);
        String dtoAsString = objectMapper.writeValueAsString(memberDto);

        String url = "http://localhost:" + port + "/api/v2/factory/{factoryNo}/user";
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.post(url, "2") //method: create
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoAsString))
                .andExpect(status().is5xxServerError())
                .andReturn().getResponse().getContentAsString();

        log.info("errorResult = "+contentAsString);
        ErrorResult errorResult = objectMapper.readValue(contentAsString, ErrorResult.class);
        assertThat(errorResult.getCode()).isEqualTo("ERROR");
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 사용자수정시 주요항목을 모두 비우고 수정 테스트")
    void modifyByMockMvcWithNoData() throws Exception {
        MemberDto memberDto = new MemberDto();
        memberDto.setUserNo("999"); //사용자번호는 무시되어야 한다.
        memberDto.setUserId("kfactory");
        memberDto.setUserPw(null);
        memberDto.setUserName(null);
        memberDto.setDefaultDashboardNo(null);
        memberDto.setAuth(null);
        String dtoAsString = objectMapper.writeValueAsString(memberDto);

        String url = "http://localhost:" + port + "/api/v2/factory/{factoryNo}/user";
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.put(url, "2") //method: create
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoAsString))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Result result = objectMapper.readValue(contentAsString, Result.class);
        Member member = memberService.find(Long.valueOf(result.userNo));
        assertThat(member.getUserName()).isEqualTo("Factory Manager");
        assertThat(member.getUserPw()).isEqualTo("admin");
        assertThat(member.getFactory().getFactoryNo()).isEqualTo(2);
        assertThat(member.getAuthList().get(0).getAuth()).isEqualTo(MemberAuthEnum.ROLE_FACTORY_MANAGER); //사용자권한은 변경되지 않아야 한다.
    }

}