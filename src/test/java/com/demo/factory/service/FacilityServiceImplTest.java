package com.demo.factory.service;

import com.demo.factory.domain.DataSaveEnum;
import com.demo.factory.domain.Facility;
import com.demo.factory.dto.facility.FacilityDto;
import com.demo.factory.dto.facility.FacilityDtoForList;
import com.demo.factory.repository.facility.FacilityRepository;
import com.demo.factory.service.facility.FacilityService;
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
class FacilityServiceImplTest {
    @Autowired
    FacilityService facilityService;

    @Autowired
    FacilityRepository facilityRepository; //설비아이디로 검색하는 테스트

    @Test
    @DisplayName("서비스 등록 테스트")
    void create() throws Exception{
        FacilityDto dtoForRegister = new FacilityDto();
        dtoForRegister.setFacilityName("설비코드");
        dtoForRegister.setDataSaveYn("Y");
        dtoForRegister.setFactoryNo("2");
        dtoForRegister.setFacilityCodeNo("1");
        Long facilityNo = facilityService.create(dtoForRegister);
        Facility resultForRegister = facilityService.find(facilityNo,2L);
        assertThat(resultForRegister.getFacilityNo()).isNotNull();
        assertThat(resultForRegister.getFacilityName()).isEqualTo("설비코드");
        assertThat(resultForRegister.getDataSaveYn()).isEqualTo(DataSaveEnum.Y);
        assertThat(resultForRegister.getFactory().getFactoryNo()).isEqualTo(2);
        assertThat(resultForRegister.getFacilityCode().getFacilityCodeNo()).isEqualTo(1L);
    }

    @Test
    @DisplayName("서비스 수정 테스트")
    void modify() throws Exception{
        Facility facility = facilityService.find(1L,1L);
        FacilityDto dtoForModify = new FacilityDto(facility);
        dtoForModify.setFacilityName("수정됨");
        dtoForModify.setDataSaveYn("N");
        Long facilityNo = facilityService.modify(dtoForModify);
        Facility resultForModify = facilityService.find(facilityNo,1L);
        assertThat(resultForModify.getFacilityNo()).isEqualTo(1);
        assertThat(resultForModify.getFacilityName()).isEqualTo("수정됨");
        assertThat(resultForModify.getDataSaveYn()).isEqualTo(DataSaveEnum.N);
        assertThat(resultForModify.getFactory().getFactoryNo()).isEqualTo(1);
        assertThat(resultForModify.getFacilityCode().getFacilityCodeNo()).isEqualTo(1L);
    }

    @Test
    @DisplayName("서비스 삭제 테스트")
    void remove() throws Exception {
        facilityService.removeByParent(2L,1L);
        Facility facility = facilityService.find(1L,2L);
        assertThat(facility.getFacilityNo()).isNull();
    }

    @Test
    @DisplayName("서비스 리스트 테스트")
    void list() throws Exception {
        PageRequestVO pageRequestVO = new PageRequestVO();
        pageRequestVO.setFactoryNo(2L);
        Page<FacilityDtoForList> list = facilityService.list(pageRequestVO);
        FacilityDtoForList dto = list.getContent().get(0);
        assertThat(dto.getFacilityNo()).isNotEmpty();
        assertThat(dto.getFacilityName()).isNotEmpty();
        assertThat(dto.getFacilityCodeNo()).isNotEmpty();

    }

    @Test
    @DisplayName("서비스 설비이름으로 조회 테스트")
    void findByFacilityName() {
        Facility facility = facilityRepository.findByFacilityNameAndFactoryNo("noExist",1L).orElse(new Facility());
        assertThat(facility.getFacilityNo()).isNull();
    }
}
