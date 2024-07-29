package com.demo.factory.service;

import com.demo.factory.domain.FacilityCode;
import com.demo.factory.dto.facilityCode.FacilityCodeDto;
import com.demo.factory.dto.facilityCode.FacilityCodeDtoForList;
import com.demo.factory.service.facilityCode.FacilityCodeService;
import com.demo.factory.vo.PageRequestVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@Sql(scripts = {"/data/insert.sql"},
        config = @SqlConfig(encoding = "utf-8", transactionMode = SqlConfig.TransactionMode.ISOLATED))
class FacilityCodeServiceImplTest {
    @Autowired
    FacilityCodeService facilityCodeService;

    @Test
    @DisplayName("서비스 등록 테스트")
    void create() throws Exception {
        FacilityCodeDto dtoForRegister = new FacilityCodeDto();
        dtoForRegister.setFacilityCode("testCode");
        dtoForRegister.setFacilityCodeName("서버");
        dtoForRegister.setFacilityCodeInfo("{테스트}");
        dtoForRegister.setFactoryNo("2");
        Long facilityCodeNo = facilityCodeService.create(dtoForRegister);
        FacilityCode resultForRegister = facilityCodeService.find(facilityCodeNo,2L);
        assertThat(resultForRegister.getFacilityCodeNo()).isNotNull();
        assertThat(resultForRegister.getFacilityCode()).isEqualTo("testCode");
        assertThat(resultForRegister.getFacilityCodeName()).isEqualTo("서버");
        assertThat(resultForRegister.getFacilityCodeInfo()).isEqualTo("{테스트}");
    }
    @Test
    @DisplayName("서비스 수정 테스트")
    void modify() throws Exception {
        FacilityCode facilityCode = facilityCodeService.find(1L,2L);
        FacilityCodeDto dtoForModify = new FacilityCodeDto(facilityCode);
        dtoForModify.setFacilityCode("수정됨");
        dtoForModify.setFacilityCodeName("수정됨");
        dtoForModify.setFacilityCodeInfo("수정됨");
        Long facilityCodeNo = facilityCodeService.modify(dtoForModify);
        FacilityCode resultForModify = facilityCodeService.find(facilityCodeNo,2L);
        assertThat(resultForModify.getFacilityCode()).isEqualTo("수정됨");
        assertThat(resultForModify.getFacilityCodeName()).isEqualTo("수정됨");
        assertThat(resultForModify.getFacilityCodeInfo()).isEqualTo("수정됨");
    }

    @Test
    @DisplayName("서비스 삭제 테스트")
    void remove() throws Exception {
        facilityCodeService.remove(2L,2L);
        FacilityCode resultForRemove = facilityCodeService.find(2L,2L);
        assertThat(resultForRemove.getFacilityCodeNo()).isNull();
    }

    @Test
    @DisplayName("서비스 리스트 테스트")
    void list() throws Exception {
        PageRequestVO pageRequestVO = new PageRequestVO();
        pageRequestVO.setFactoryNo(2L);
        Page<FacilityCodeDtoForList> list = facilityCodeService.list(pageRequestVO);
        FacilityCodeDtoForList dto = list.getContent().get(0);
        log.info("facilityCodeDto {} {} {}",dto.getFacilityCodeNo(),dto.getFacilityCode(),dto.getFacilityCodeName());
        assertThat(dto.getFacilityCodeNo()).isNotEmpty();
        assertThat(dto.getFacilityCode()).isNotEmpty();
        assertThat(dto.getFacilityCodeName()).isNotEmpty();
        assertThat(dto.getFacilityCodeInfo()).isNotEmpty();
    }

}