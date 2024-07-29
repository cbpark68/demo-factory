package com.demo.factory.controller.api;

import com.demo.factory.common.exception.ErrorResult;
import com.demo.factory.domain.DataSaveEnum;
import com.demo.factory.domain.Facility;
import com.demo.factory.dto.facility.FacilityDto;
import com.demo.factory.dto.facility.FacilityDtoForList;
import com.demo.factory.service.facility.FacilityService;
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
class ApiFacilityControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    FacilityService facilityService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    public record Result(String facilityNo) {
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
        String url = "http://localhost:" + port + "/api/v0/factory/{factoryNo}/facility/list?page=1&size=10";
        String contentAsString = mockMvc.perform(RestDocumentationRequestBuilders.get(url,"2")) //method: list
                .andExpect(status().isOk())
                .andDo(document("/facility/list",
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
                                        fieldWithPath("page.content[].facilityNo").description("설비번호"),
                                        fieldWithPath("page.content[].facilityName").description("설비이름"),
                                        fieldWithPath("page.content[].facilityCodeNo").description("설비코드번호"),
                                        fieldWithPath("page.content[].dataSaveYn").description("데이터저장여부"),
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

        FacilityDtoForList facilityDtoForList = objectMapper.treeToValue(objectMapper.readTree(contentAsString).get("page").get("content").get(0), FacilityDtoForList.class);
        assertThat(facilityDtoForList.getFacilityNo()).isNotEmpty();
        assertThat(facilityDtoForList.getFacilityName()).isNotEmpty();
        assertThat(facilityDtoForList.getDataSaveYn()).isIn("Y", "N");
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 등록 테스트")
    void create() throws Exception {
        FacilityDto facilityDto = new FacilityDto();
        facilityDto.setFacilityNo("999"); //dto의 팩토리는 무시되어야 한다.
        facilityDto.setFacilityName("신규등록");
        facilityDto.setDataSaveYn("Y");
        facilityDto.setFacilityCodeNo("1");
        String facilityDtoAsString = objectMapper.writeValueAsString(facilityDto);

        String url = "http://localhost:" + port + "/api/v0/factory/{factoryNo}/facility";
        String contentAsString = mockMvc.perform(RestDocumentationRequestBuilders.post(url, "2") //method: create
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(facilityDtoAsString))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("facility/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("factoryNo").description("팩토리번호")
                        ),
                        requestFields(
                                fieldWithPath("facilityNo").description("설비번호: 자동생성"),
                                fieldWithPath("facilityName").description("설비이름"),
                                fieldWithPath("facilityCodeNo").description("설비코드번호"),
                                fieldWithPath("dataSaveYn").description("데이터저장여부"),
                                fieldWithPath("factoryNo").description("팩토리번호: N/A"),
                                fieldWithPath("createDatetime").description("최초등록일시: 자동생성"),
                                fieldWithPath("lastModifyDatetime").description("최종수정일시: 자동생성")
                        ),
                        responseFields(
                                fieldWithPath("facilityNo").description("설비번호: 자동생성")
                        )
                ))
                .andReturn().getResponse().getContentAsString();

        Result result = objectMapper.readValue(contentAsString, Result.class);
        Facility resultForRegister = facilityService.find(Long.valueOf(result.facilityNo),2L);
        assertThat(resultForRegister.getFacilityNo()).isNotEqualTo(999L); //대시코드번호는 등록되지 않아야 한다.
        assertThat(resultForRegister.getFactory().getFactoryNo()).isEqualTo(2L); //팩토리번호는 변경되지 않아야 한다.
        assertThat(resultForRegister.getFacilityCode().getFacilityCodeNo()).isEqualTo(1);
        assertThat(resultForRegister.getFacilityName()).isEqualTo("신규등록");
        assertThat(resultForRegister.getDataSaveYn()).isEqualTo(DataSaveEnum.Y);
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 수정 테스트")
    void modify() throws Exception {
        FacilityDto facilityDto = new FacilityDto();
        facilityDto.setFacilityNo("1");
        facilityDto.setFacilityName("수정됨"); //수정되면 안된다.
        facilityDto.setDataSaveYn("N");
        facilityDto.setFactoryNo("999"); //dto의 팩토리는 무시되어야 한다.
        facilityDto.setFacilityCodeNo("1"); //설비코드번호는 정확해야 한다.
        String facilityDtoAsString = objectMapper.writeValueAsString(facilityDto);

        String url = "http://localhost:" + port + "/api/v0/factory/{factoryNo}/facility";
        String contentAsString = mockMvc.perform(RestDocumentationRequestBuilders.put(url, "1") //method: modify
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(facilityDtoAsString))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("facility/modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("factoryNo").description("팩토리번호")
                        ),
                        requestFields(
                                fieldWithPath("facilityNo").description("설비번호: 수정대상"),
                                fieldWithPath("facilityName").description("설비이름"),
                                fieldWithPath("facilityCodeNo").description("설비코드번호"),
                                fieldWithPath("dataSaveYn").description("데이터저장여부"),
                                fieldWithPath("factoryNo").description("팩토리번호: N/A"),
                                fieldWithPath("createDatetime").description("최초등록일시: 자동생성"),
                                fieldWithPath("lastModifyDatetime").description("최종수정일시: 자동생성")
                        ),
                        responseFields(
                                fieldWithPath("facilityNo").description("설비번호: 자동생성")
                        )
                ))
                .andReturn().getResponse().getContentAsString();

        Result result = objectMapper.readValue(contentAsString, Result.class);
        Facility resultForModify = facilityService.find(Long.valueOf(result.facilityNo),1L);
        assertThat(resultForModify.getFacilityName()).isEqualTo("수정됨");
        assertThat(resultForModify.getDataSaveYn()).isEqualTo(DataSaveEnum.N);
        assertThat(resultForModify.getFacilityCode().getFacilityCodeNo()).isEqualTo(1L);
        assertThat(resultForModify.getFactory().getFactoryNo()).isEqualTo(1L); //팩토리번호는 변경되지 않아야 한다.
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 삭제 테스트")
    void remove() throws Exception {
        String url = "http://localhost:" + port + "/api/v0/factory/{factoryNo}/facility/{facilityNo}";
        mockMvc.perform(RestDocumentationRequestBuilders.delete(url, "2","1")) //method: remove
                .andExpect(status().is2xxSuccessful())
                .andDo(document("facility/remove",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("factoryNo").description("팩토리번호"),
                                parameterWithName("facilityNo").description("설비번호")
                        )
                ))
                .andReturn().getResponse().getContentAsString();

        Facility facility = facilityService.find(1L,2L);
        assertThat(facility.getFacilityNo()).isNull();
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 설비정보없는 등록 테스트")
    void createWithoutData() throws Exception {
        FacilityDto facilityDto = new FacilityDto();
        facilityDto.setFacilityNo(null); //dto의 팩토리는 무시되어야 한다.
        facilityDto.setFacilityName(null);
        facilityDto.setDataSaveYn("Y");
        facilityDto.setFacilityCodeNo("2");
        String facilityDtoAsString = objectMapper.writeValueAsString(facilityDto);

        String url = "http://localhost:" + port + "/api/v0/factory/{factoryNo}/facility";
        String contentAsString = mockMvc.perform(RestDocumentationRequestBuilders.post(url, "2") //method: create
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(facilityDtoAsString))
                .andExpect(status().is5xxServerError())
                .andReturn().getResponse().getContentAsString();

        log.info("errorResult = "+contentAsString);
        ErrorResult errorResult = objectMapper.readValue(contentAsString, ErrorResult.class);
        assertThat(errorResult.getCode()).isEqualTo("ERROR");
    }

    @Test
    @WithMockUser(username = "admin",password = "1234",roles = {"ADMIN"})
    @DisplayName("api 설비정보없는 수정 테스트")
    void modifyByNoData() throws Exception {
        FacilityDto facilityDto = new FacilityDto();
        facilityDto.setFacilityNo("1");
        facilityDto.setFacilityName(null);
        facilityDto.setDataSaveYn("Y");
        facilityDto.setFactoryNo(null); //dto의 팩토리는 무시되어야 한다.
        facilityDto.setFacilityCodeNo("1"); //설비코드번호는 정확해야 한다.
        String facilityDtoAsString = objectMapper.writeValueAsString(facilityDto);

        String url = "http://localhost:" + port + "/api/v0/factory/{factoryNo}/facility";
        String contentAsString = mockMvc.perform(RestDocumentationRequestBuilders.put(url, "1") //method: modify
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(facilityDtoAsString))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        log.info("errorResult = "+contentAsString);
        Result result = objectMapper.readValue(contentAsString, Result.class);
        Facility resultForModify = facilityService.find(Long.valueOf(result.facilityNo),1L);
        assertThat(resultForModify.getFacilityName()).isEqualTo("line1");
        assertThat(resultForModify.getDataSaveYn()).isEqualTo(DataSaveEnum.Y);
        assertThat(resultForModify.getFacilityCode().getFacilityCodeNo()).isEqualTo(1L);
        assertThat(resultForModify.getFactory().getFactoryNo()).isEqualTo(1L); //팩토리번호는 변경되지 않아야 한다.
    }

}