package com.demo.factory.controller.api;

import com.demo.factory.common.exception.ErrorResult;
import com.demo.factory.domain.Factory;
import com.demo.factory.domain.FactoryStatusEnum;
import com.demo.factory.domain.Member;
import com.demo.factory.domain.MemberAuthEnum;
import com.demo.factory.dto.factory.FactoryDtoForList;
import com.demo.factory.dto.factory.FactoryDtoForManager;
import com.demo.factory.service.member.MemberService;
import com.demo.factory.service.factory.FactoryService;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@Sql(scripts = {"/data/insert.sql"},
        config = @SqlConfig(encoding = "utf-8", transactionMode = SqlConfig.TransactionMode.ISOLATED))
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class ApiFactoryControllerByMockMvcTest {
    @LocalServerPort
    private int port;

    @Autowired
    private FactoryService factoryService;

    @Autowired
    private MemberService memberService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    public record Result(String factoryNo, String userNo) {
    }

    @BeforeEach
    void setUpForMockMvc(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 리스트 테스트")
    void listByMockMvc() throws Exception {
        String url = "http://localhost:" + port + "/api/v1/factory/list?page=1&size=2";
        String resultAsString = mockMvc.perform(RestDocumentationRequestBuilders.get(url))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("/factory/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page").description("페이지번호"),
                                parameterWithName("size").description("페이지당 최대 데이터건수")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("page").description("페이지객체"),
                                        fieldWithPath("page.content[]").description("페이지데이터배열"),
                                        fieldWithPath("page.content[].factoryNo").description("팩토리번호"),
                                        fieldWithPath("page.content[].factoryName").description("팩토리이름"),
                                        fieldWithPath("page.content[].factoryStatus").description("팩토리상태"),
                                        fieldWithPath("page.content[].userId").description("팩토리관리자아이디"),
                                        fieldWithPath("page.content[].userPw").description("팩토리관리자비번"),
                                        fieldWithPath("page.content[].userName").description("팩토리관리자이름"),
                                        fieldWithPath("page.content[].logoFileName").description("로고파일이름"),
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
        FactoryDtoForList factoryDtoForList = objectMapper.treeToValue(objectMapper.readTree(resultAsString).get("page").get("content").get(0), FactoryDtoForList.class);
        assertThat(factoryDtoForList.getFactoryNo()).isNotEmpty();
        assertThat(factoryDtoForList.getFactoryName()).isNotEmpty();
        assertThat(factoryDtoForList.getFactoryStatus()).isIn(FactoryStatusEnum.ACTIVE, FactoryStatusEnum.STANDBY);
        assertThat(factoryDtoForList.getUserId()).isNotEmpty();
        assertThat(factoryDtoForList.getUserPw()).isNotEmpty();
        assertThat(factoryDtoForList.getUserName()).isNotEmpty();
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 등록 테스트")
    void createdByMockMvc() throws Exception {
        String url = "http://localhost:" + port + "/api/v0/factory";

        FactoryDtoForManager factoryDto = new FactoryDtoForManager();
        factoryDto.setFactoryNo("99999"); //팩토리번호는 무시되어야 한다.
        factoryDto.setFactoryName("테스트팩토리");
        factoryDto.setFactoryStatus(FactoryStatusEnum.ACTIVE);
        factoryDto.setUserId("testFactoryManager");
        factoryDto.setUserPw("1234");
        factoryDto.setUserName("테스트매니저");

        String factoryDtoAsString = objectMapper.writeValueAsString(factoryDto);

        MockMultipartFile factoryPart = new MockMultipartFile("factory", "", "application/json",
                factoryDtoAsString.getBytes(StandardCharsets.UTF_8));

        MockMultipartFile logoFilePart = new MockMultipartFile(
                "logoFile","min_logo.png","multipart/form-data",
                new FileInputStream("c:/java/min_logo.png"));

        String contentAsString = mockMvc.perform(multipart(url) //method: create
                        .file(logoFilePart)
                        .file(factoryPart)
                        .contentType("multipart/form-data"))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("/factory/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("factory").description("팩토리 정보: json타입"),
                                partWithName("logoFile").description("로고파일: 바이너리타입")
                        ),
                        requestPartFields(
                                "factory",
                                fieldWithPath("factoryNo").description("팩토리번호: 자동생성"),
                                fieldWithPath("factoryName").description("팩토리이름"),
                                fieldWithPath("factoryStatus").description("팩토리상태: STANDBY/ACTIVE"),
                                fieldWithPath("userId").description("팩토리관리자 아이디"),
                                fieldWithPath("userPw").description("팩토리관리자 비번"),
                                fieldWithPath("userName").description("팩토리관리자 이름"),
                                fieldWithPath("logoFileName").description("로고파일이름: logo.png자동생성"),
                                fieldWithPath("createDatetime").description("최초등록일시: 자동생성"),
                                fieldWithPath("lastModifyDatetime").description("최종수정일시: 자동생성")
                        ),
                        requestPartBody("logoFile" ),
                        responseFields(
                                fieldWithPath("factoryNo").description("팩토리번호: 정상처리시 발번"),
                                fieldWithPath("userNo").description("사용자번호: 정상처리시 발번")
                        )
                ))
                .andReturn().getResponse().getContentAsString();

        Result result = objectMapper.readValue(contentAsString, Result.class);
        Factory factory = factoryService.find(Long.valueOf(result.factoryNo));
        assertThat(factory.getFactoryName()).isEqualTo("테스트팩토리");
        assertThat(factory.getFactoryStatus()).isEqualTo(FactoryStatusEnum.ACTIVE);
        Member member = memberService.find(Long.valueOf(result.userNo));
        assertThat(member.getUserId()).isEqualTo("testFactoryManager");
        assertThat(member.getUserName()).isEqualTo("테스트매니저");
        assertThat(member.getUserPw()).isEqualTo("1234");
        assertThat(member.getAuthList().get(0).getAuth()).isEqualTo(MemberAuthEnum.ROLE_FACTORY_MANAGER); //사용자권한은 반드시 팩토리매니저이어야 함

        url = "http://localhost:" + port + "/api/v1/factory/"+result.factoryNo()+"/logo";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().is(201));
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 수정 테스트")
    void modifiedByMockMvc() throws Exception {
        String url = "http://localhost:" + port + "/api/v0/factory";

        FactoryDtoForManager factoryDto = new FactoryDtoForManager();
        factoryDto.setFactoryNo("2");
        factoryDto.setFactoryName("테스트팩토리");
        factoryDto.setFactoryStatus(FactoryStatusEnum.ACTIVE);
        factoryDto.setUserId("kfactory");
        factoryDto.setUserPw("12341234");
        factoryDto.setUserName("테스트매니저");

        String strDtoForString = objectMapper.writeValueAsString(factoryDto);

        MockMultipartFile factoryPart = new MockMultipartFile("factory", "", "application/json",
                strDtoForString.getBytes(StandardCharsets.UTF_8));

        MockMultipartFile logoFilePart = new MockMultipartFile(
                "logoFile","logo0001.png","multipart/form-data",
                new FileInputStream("c:/java/min_logo.png"));

        MockMultipartHttpServletRequestBuilder builder = RestDocumentationRequestBuilders.multipart(url);

        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        String contentAsString = mockMvc.perform(builder  //method: modify
                        .file(logoFilePart)
                        .file(factoryPart)
                        .contentType("multipart/form-data"))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("/factory/modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("factory").description("팩토리 정보: json타입"),
                                partWithName("logoFile").description("로고파일: 바이너리타입")
                        ),
                        requestPartFields(
                                "factory",
                                fieldWithPath("factoryNo").description("팩토리번호: 수정대상"),
                                fieldWithPath("factoryName").description("팩토리이름"),
                                fieldWithPath("factoryStatus").description("팩토리상태: STANDBY/ACTIVE"),
                                fieldWithPath("userId").description("팩토리관리자 아이디"),
                                fieldWithPath("userPw").description("팩토리관리자 비번"),
                                fieldWithPath("userName").description("팩토리관리자 이름"),
                                fieldWithPath("logoFileName").description("로고파일이름: logo.png자동생성"),
                                fieldWithPath("createDatetime").description("최초등록일시: 자동생성"),
                                fieldWithPath("lastModifyDatetime").description("최종수정일시: 자동생성")
                        ),
                        requestPartBody("logoFile" ),
                        responseFields(
                                fieldWithPath("factoryNo").description("팩토리번호: 정상처리시 발번"),
                                fieldWithPath("userNo").description("사용자번호: 정상처리시 발번")
                        )
                ))
                .andReturn().getResponse().getContentAsString();

        Result result = objectMapper.readValue(contentAsString, Result.class);
        Factory factory = factoryService.find(Long.valueOf(result.factoryNo));
        assertThat(factory.getFactoryNo()).isEqualTo(2L);
        assertThat(factory.getFactoryName()).isEqualTo("테스트팩토리");
        assertThat(factory.getFactoryStatus()).isEqualTo(FactoryStatusEnum.ACTIVE); //변경되지 않아야 정상
        Member member = memberService.find(Long.valueOf(result.userNo));
        assertThat(member.getUserId()).isEqualTo("kfactory");
        assertThat(member.getUserName()).isEqualTo("테스트매니저");
        assertThat(member.getUserPw()).isEqualTo("12341234");
        assertThat(member.getAuthList().get(0).getAuth()).isEqualTo(MemberAuthEnum.ROLE_FACTORY_MANAGER); //사용자권한은 반드시 팩토리매니저이어야 함

        url = "http://localhost:" + port + "/api/v1/factory/"+result.factoryNo()+"/logo";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().is(201));
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 삭제 테스트")
    void removeByMockMvc() throws Exception {
        String url = "http://localhost:" + port + "/api/v1/factory/{factoryNo}";
        mockMvc.perform(RestDocumentationRequestBuilders.delete(url,"2")) //method: remove
                        .andExpect(status().is2xxSuccessful())
                        .andDo(document("/factory/remove",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("factoryNo").description("팩토리번호: 삭제대상")
                                )
                        )
        )
        .andReturn().getResponse().getContentAsString();

        Factory factory = factoryService.find(2L);
        assertThat(factory.getFactoryNo()).isNull();
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 모델파일 업로드")
    void createModelByMockMvc() throws Exception {
        String url = "http://localhost:" + port + "/api/v0/factory/{factoryNo}";

        MockMultipartFile modelFilePart = new MockMultipartFile(
                "modelFile","factory_1_model_info.zip","multipart/form-data",
                new FileInputStream("c:/java/factory_1_model_info.zip"));

        MockMultipartHttpServletRequestBuilder builder = RestDocumentationRequestBuilders.multipart(url,"1");

        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PATCH");
                return request;
            }
        });

        mockMvc.perform(builder
                        .file(modelFilePart)
                        .contentType("multipart/form-data"))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("/factory/model",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("modelFile").description("모델파일: 폴더를 압축한 zip파일")
                        ),
                        requestPartBody("modelFile"),
                        pathParameters(
                                parameterWithName("factoryNo").description("팩토리번호: 업로드대상")
                        )
                ))
                .andReturn().getResponse().getContentAsString();

        url = "http://localhost:" + port + "/api/v0/factory/{factoryNo}/model/{modelFileName}";
        mockMvc.perform(MockMvcRequestBuilders.get(url,"1","factory_1_model_info.zip"))
                .andExpect(status().is(201));
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 존재하지 않는 팩토리 삭제 테스트")
    void removeByMockMvcForNotExist() throws Exception {
        String url = "http://localhost:" + port + "/api/v1/factory/0";
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(contentAsString).isEmpty();
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 로고없이 팩토리 등록 테스트")
    void createdByMockMvcWithoutLogo() throws Exception {
        String url = "http://localhost:" + port + "/api/v0/factory";

        FactoryDtoForManager factoryDto = new FactoryDtoForManager();
        factoryDto.setFactoryNo("99999"); //팩토리번호는 무시되어야 한다.
        factoryDto.setFactoryName("테스트팩토리");
        factoryDto.setFactoryStatus(FactoryStatusEnum.ACTIVE);
        factoryDto.setUserId("testFactoryManager");
        factoryDto.setUserPw("1234");
        factoryDto.setUserName("테스트매니저");

        String factoryDtoAsString = objectMapper.writeValueAsString(factoryDto);

        MockMultipartFile factoryPart = new MockMultipartFile("factory", "", "application/json",
                factoryDtoAsString.getBytes(StandardCharsets.UTF_8));

        String contentAsString = mockMvc.perform(multipart(url) //method: create
                        .file(factoryPart)
                        .contentType("multipart/form-data"))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        Result result = objectMapper.readValue(contentAsString, Result.class);
        Factory factory = factoryService.find(Long.valueOf(result.factoryNo));
        assertThat(factory.getFactoryName()).isEqualTo("테스트팩토리");
        assertThat(factory.getFactoryStatus()).isEqualTo(FactoryStatusEnum.ACTIVE);
        Member member = memberService.find(Long.valueOf(result.userNo));
        assertThat(member.getUserId()).isEqualTo("testFactoryManager");
        assertThat(member.getUserName()).isEqualTo("테스트매니저");
        assertThat(member.getUserPw()).isEqualTo("1234");
        assertThat(member.getAuthList().get(0).getAuth()).isEqualTo(MemberAuthEnum.ROLE_FACTORY_MANAGER); //사용자권한은 반드시 팩토리매니저이어야 함

    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 로고없이 팩토리 수정 테스트")
    void modifiedByMockMvcWithoutLogo() throws Exception {
        String url = "http://localhost:" + port + "/api/v0/factory";

        FactoryDtoForManager factoryDto = new FactoryDtoForManager();
        factoryDto.setFactoryNo("2");
        factoryDto.setFactoryName("테스트팩토리");
        factoryDto.setFactoryStatus(FactoryStatusEnum.ACTIVE);
        factoryDto.setUserId("kfactory");
        factoryDto.setUserPw("admin");
        factoryDto.setUserName("테스트매니저");

        String strDtoForString = objectMapper.writeValueAsString(factoryDto);

        MockMultipartFile factoryPart = new MockMultipartFile("factory", "", "application/json",
                strDtoForString.getBytes(StandardCharsets.UTF_8));

        MockMultipartHttpServletRequestBuilder builder = RestDocumentationRequestBuilders.multipart(url);

        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        String contentAsString = mockMvc.perform(builder  //method: modify
                        .file(factoryPart)
                        .contentType("multipart/form-data"))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        Result result = objectMapper.readValue(contentAsString, Result.class);
        Factory factory = factoryService.find(Long.valueOf(result.factoryNo));
        assertThat(factory.getFactoryNo()).isEqualTo(2L);
        assertThat(factory.getFactoryName()).isEqualTo("테스트팩토리");
        assertThat(factory.getFactoryStatus()).isEqualTo(FactoryStatusEnum.ACTIVE); //변경되지 않아야 정상
        Member member = memberService.find(Long.valueOf(result.userNo));
        assertThat(member.getUserId()).isEqualTo("kfactory");
        assertThat(member.getUserName()).isEqualTo("테스트매니저");
        assertThat(member.getUserPw()).isEqualTo("admin");
        assertThat(member.getAuthList().get(0).getAuth()).isEqualTo(MemberAuthEnum.ROLE_FACTORY_MANAGER); //사용자권한은 반드시 팩토리매니저이어야 함

    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 로고없이 팩토리 등록 테스트")
    void createdByMockMvcWithoutManager() throws Exception {
        String url = "http://localhost:" + port + "/api/v0/factory";

        FactoryDtoForManager factoryDto = new FactoryDtoForManager();
        factoryDto.setFactoryNo("99999"); //팩토리번호는 무시되어야 한다.
        factoryDto.setFactoryName("테스트팩토리");
        factoryDto.setFactoryStatus(FactoryStatusEnum.ACTIVE);
        factoryDto.setUserId("testFactoryManager");
        factoryDto.setUserPw("1234"); //공백일수 없다.
        factoryDto.setUserName(""); //공백일수 없다.

        String factoryDtoAsString = objectMapper.writeValueAsString(factoryDto);

        MockMultipartFile factoryPart = new MockMultipartFile("factory", "", "application/json",
                factoryDtoAsString.getBytes(StandardCharsets.UTF_8));

        String contentAsString = mockMvc.perform(multipart(url) //method: create
                        .file(factoryPart)
                        .contentType("multipart/form-data"))
                .andExpect(status().is5xxServerError())
                .andReturn().getResponse().getContentAsString();

        log.info("content = "+contentAsString);
        ErrorResult result = objectMapper.readValue(contentAsString, ErrorResult.class);
        assertThat(result.getCode()).isEqualTo("ERROR");
    }


    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 팩토리정보없이 로고만으로 팩토리 등록 테스트")
    void createdByMockMvcWithoutDto() throws Exception {
        String url = "http://localhost:" + port + "/api/v0/factory";

        FactoryDtoForManager factoryDto = new FactoryDtoForManager();

        String factoryDtoAsString = objectMapper.writeValueAsString(factoryDto);

        MockMultipartFile factoryPart = new MockMultipartFile("factory", "", "application/json",
                factoryDtoAsString.getBytes(StandardCharsets.UTF_8));

        MockMultipartFile logoFilePart = new MockMultipartFile(
                "logoFile","logo0001.png","multipart/form-data",
                new FileInputStream("c:/java/min_logo.png"));

        String contentAsString = mockMvc.perform(multipart(url) //method: create
                        .file(logoFilePart)
                        .file(factoryPart)
                        .contentType("multipart/form-data"))
                .andExpect(status().is5xxServerError())
                .andReturn().getResponse().getContentAsString();

        log.info("content = "+contentAsString);
        ErrorResult result = objectMapper.readValue(contentAsString, ErrorResult.class);
        assertThat(result.getCode()).isEqualTo("ERROR");
    }


}