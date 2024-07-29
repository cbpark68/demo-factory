package com.demo.factory.dto.facilityCode;

import com.demo.factory.domain.FacilityCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FacilityCodeDto {

    private String facilityCodeNo;

    private String facilityCode;

    private String facilityCodeName;

    private String facilityCodeInfo;

    private String factoryNo;

    private String createDatetime;

    private String lastModifyDatetime;

    public FacilityCodeDto(FacilityCode facilityCode) {
        this.facilityCodeNo = String.valueOf(facilityCode.getFacilityCodeNo());
        this.facilityCode = facilityCode.getFacilityCode();
        this.facilityCodeName = facilityCode.getFacilityCodeName();
        this.facilityCodeInfo = facilityCode.getFacilityCodeInfo();
        this.factoryNo = String.valueOf(facilityCode.getFactory().getFactoryNo());
        this.createDatetime = String.valueOf(facilityCode.getCreateDatetime());
        this.lastModifyDatetime = String.valueOf(facilityCode.getLastModifyDatetime());
    }

    public FacilityCodeDto(String factoryNo, String facilityCode, String facilityCodeName, String facilityCodeInfo) {
        this.factoryNo = factoryNo;
        this.facilityCode = facilityCode;
        this.facilityCodeName = facilityCodeName;
        this.facilityCodeInfo = facilityCodeInfo;
    }


}
