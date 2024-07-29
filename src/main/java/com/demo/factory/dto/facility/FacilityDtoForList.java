package com.demo.factory.dto.facility;


import com.demo.factory.domain.Facility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FacilityDtoForList {

    private String facilityNo;

    private String facilityName;

    private String facilityCodeNo;

    private String dataSaveYn;

    private String createDatetime;

    private String lastModifyDatetime;

    public FacilityDtoForList(Facility facility) {
        this.facilityNo = String.valueOf(facility.getFacilityNo());
        this.facilityName = facility.getFacilityName();
        this.facilityCodeNo = String.valueOf(facility.getFacilityCode().getFacilityCodeNo());
        this.dataSaveYn = String.valueOf(facility.getDataSaveYn());
        this.createDatetime = String.valueOf(facility.getCreateDatetime());
        this.lastModifyDatetime = String.valueOf(facility.getLastModifyDatetime());
    }

}
