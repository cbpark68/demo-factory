package com.demo.factory.controller.api;

import com.demo.factory.domain.FacilityCode;
import com.demo.factory.dto.facilityCode.FacilityCodeDto;
import com.demo.factory.dto.facilityCode.FacilityCodeDtoForList;
import com.demo.factory.service.facilityCode.FacilityCodeService;
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
class ApiFacilityCodeControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    FacilityCodeService facilityCodeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    public record Result(String facilityCodeNo) {
    }

    @BeforeEach
    void setUpForMockmvc(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 리스트 테스트")
    void list() throws Exception {
        String url = "http://localhost:" + port + "/api/v0/factory/{factoryNo}/facilityCode/list?page=1&size=10";
        String contentAsString = mockMvc.perform(RestDocumentationRequestBuilders.get(url,"2")) //method: list
                .andExpect(status().isOk())
                .andDo(document("/facilityCode/list",
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
                                        fieldWithPath("page.content[].facilityCodeNo").description("설비코드번호"),
                                        fieldWithPath("page.content[].facilityCode").description("설비코드"),
                                        fieldWithPath("page.content[].facilityCodeName").description("설비코드이름"),
                                        fieldWithPath("page.content[].facilityCodeInfo").description("설비코드정보"),
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

        FacilityCodeDtoForList facilityCodeDtoForList = objectMapper.treeToValue(objectMapper.readTree(contentAsString).get("page").get("content").get(0), FacilityCodeDtoForList.class);
        assertThat(facilityCodeDtoForList.getFacilityCodeNo()).isNotEmpty();
        assertThat(facilityCodeDtoForList.getFacilityCode()).isNotEmpty();
        assertThat(facilityCodeDtoForList.getFacilityCodeName()).isNotEmpty();
        assertThat(facilityCodeDtoForList.getFacilityCodeInfo()).isNotEmpty();
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 등록 테스트")
    void create() throws Exception {
        FacilityCodeDto facilityCodeDto = new FacilityCodeDto();
        facilityCodeDto.setFacilityCodeNo("999"); //dto의 팩토리는 무시되어야 한다.
        facilityCodeDto.setFacilityCode("신규등록");
        facilityCodeDto.setFacilityCodeName("신규등록");
        facilityCodeDto.setFacilityCodeInfo("신규등록");
        String facilityCodeDtoAsString = objectMapper.writeValueAsString(facilityCodeDto);

        String url = "http://localhost:" + port + "/api/v0/factory/{factoryNo}/facilityCode";
        String contentAsString = mockMvc.perform(RestDocumentationRequestBuilders.post(url, "2") //method: create
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(facilityCodeDtoAsString))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("facilityCode/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("factoryNo").description("팩토리번호")
                        ),
                        requestFields(
                                fieldWithPath("facilityCodeNo").description("설비코드번호: 자동생성"),
                                fieldWithPath("facilityCode").description("설비코드"),
                                fieldWithPath("facilityCodeName").description("설비코드이름"),
                                fieldWithPath("facilityCodeInfo").description("설비코드정보"),
                                fieldWithPath("factoryNo").description("팩토리번호: N/A"),
                                fieldWithPath("createDatetime").description("최초등록일시: 자동생성"),
                                fieldWithPath("lastModifyDatetime").description("최종수정일시: 자동생성")
                        ),
                        responseFields(
                                fieldWithPath("facilityCodeNo").description("설비코드번호: 자동생성")
                        )
                ))
                .andReturn().getResponse().getContentAsString();

        Result result = objectMapper.readValue(contentAsString, Result.class);
        FacilityCode facilityCode = facilityCodeService.find(Long.valueOf(result.facilityCodeNo),2L);
        assertThat(facilityCode.getFacilityCodeNo()).isNotEqualTo(999L); //대시코드번호는 등록되지 않아야 한다.
        assertThat(facilityCode.getFactory().getFactoryNo()).isEqualTo(2L); //팩토리번호는 변경되지 않아야 한다.
        assertThat(facilityCode.getFacilityCode()).isEqualTo("신규등록");
        assertThat(facilityCode.getFacilityCodeName()).isEqualTo("신규등록");
        assertThat(facilityCode.getFacilityCodeInfo()).isEqualTo("신규등록");
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 수정 삭제")
    void modify() throws Exception {
        FacilityCodeDto facilityCodeDto = new FacilityCodeDto();
        facilityCodeDto.setFacilityCodeNo("1");
        facilityCodeDto.setFacilityCode("수정됨");
        facilityCodeDto.setFacilityCodeName("수정됨");
        facilityCodeDto.setFacilityCodeInfo("수정됨");
        facilityCodeDto.setFactoryNo("999"); //dto의 팩토리는 무시되어야 한다.
        String facilityCodeDtoAsString = objectMapper.writeValueAsString(facilityCodeDto);

        String url = "http://localhost:" + port + "/api/v0/factory/{factoryNo}/facilityCode";
        String contentAsString = mockMvc.perform(RestDocumentationRequestBuilders.put(url, "2") //method: modify
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(facilityCodeDtoAsString))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("facilityCode/modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("factoryNo").description("팩토리번호")
                        ),
                        requestFields(
                                fieldWithPath("facilityCodeNo").description("설비코드번호: 수정대상"),
                                fieldWithPath("facilityCode").description("설비코드"),
                                fieldWithPath("facilityCodeName").description("설비코드이름"),
                                fieldWithPath("facilityCodeInfo").description("설비코드정보"),
                                fieldWithPath("factoryNo").description("팩토리번호: N/A"),
                                fieldWithPath("createDatetime").description("최초등록일시: 자동생성"),
                                fieldWithPath("lastModifyDatetime").description("최종수정일시: 자동생성")
                        ),
                        responseFields(
                                fieldWithPath("facilityCodeNo").description("설비코드번호: 자동생성")
                        )
                ))
                .andReturn().getResponse().getContentAsString();

        Result result = objectMapper.readValue(contentAsString, Result.class);
        FacilityCode resultForModify = facilityCodeService.find(Long.valueOf(result.facilityCodeNo),2L);
        assertThat(resultForModify.getFacilityCodeNo()).isEqualTo(1L); //대시코드번호는 등록되지 않아야 한다.
        assertThat(resultForModify.getFactory().getFactoryNo()).isEqualTo(2L); //팩토리번호는 변경되지 않아야 한다.
        assertThat(resultForModify.getFacilityCode()).isEqualTo("수정됨");
        assertThat(resultForModify.getFacilityCodeName()).isEqualTo("수정됨");
        assertThat(resultForModify.getFacilityCodeInfo()).isEqualTo("수정됨");
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 삭제 테스트")
    void remove() throws Exception {
        String url = "http://localhost:" + port + "/api/v0/factory/{factoryNo}/facilityCode/{facilityCode}";
        mockMvc.perform(RestDocumentationRequestBuilders.delete(url,"2","1")) //method: remove
                .andExpect(status().is2xxSuccessful())
                .andDo(document("/facilityCode/remove",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("factoryNo").description("팩토리번호: 삭제대상"),
                                        parameterWithName("facilityCode").description("설비코드: 삭제대상")
                                )
                        )
                )
                .andReturn().getResponse().getContentAsString();

        FacilityCode facilityCode = facilityCodeService.find(1L,2L);
        assertThat(facilityCode.getFacilityCodeNo()).isNull();
    }


}