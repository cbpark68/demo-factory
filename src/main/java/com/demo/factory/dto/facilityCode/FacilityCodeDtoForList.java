package com.demo.factory.dto.facilityCode;

import com.demo.factory.domain.FacilityCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FacilityCodeDtoForList {

    private String facilityCodeNo;

    private String facilityCode;

    private String facilityCodeName;

    private String facilityCodeInfo;

    private String createDatetime;

    private String lastModifyDatetime;

    public FacilityCodeDtoForList(FacilityCode facilityCode) {
        this.facilityCodeNo = String.valueOf(facilityCode.getFacilityCodeNo());
        this.facilityCode = facilityCode.getFacilityCode();
        this.facilityCodeName = facilityCode.getFacilityCodeName();
        this.facilityCodeInfo = facilityCode.getFacilityCodeInfo();
        this.createDatetime = String.valueOf(facilityCode.getCreateDatetime());
        this.lastModifyDatetime = String.valueOf(facilityCode.getLastModifyDatetime());
    }
}
