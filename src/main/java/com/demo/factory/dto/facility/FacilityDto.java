package com.demo.factory.dto.facility;


import com.demo.factory.domain.Facility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FacilityDto {

    private String facilityNo;

    private String facilityName;

    private String facilityCodeNo;

    private String factoryNo;

    private String dataSaveYn;

    private String createDatetime;

    private String lastModifyDatetime;

    public FacilityDto(Facility facility) {
        this.facilityNo = String.valueOf(facility.getFacilityNo());
        this.facilityName = facility.getFacilityName();
        this.facilityCodeNo = String.valueOf(facility.getFacilityCode().getFacilityCodeNo());
        this.factoryNo = String.valueOf(facility.getFactory().getFactoryNo());
        this.dataSaveYn = String.valueOf(facility.getDataSaveYn());
        this.createDatetime = String.valueOf(facility.getCreateDatetime());
        this.lastModifyDatetime = String.valueOf(facility.getLastModifyDatetime());
    }

    public FacilityDto(String siteNo, String facilityCodeNo, String facilityName, String dataSaveYn) {
        this.factoryNo = siteNo;
        this.facilityCodeNo = facilityCodeNo;
        this.facilityName = facilityName;
        this.dataSaveYn = dataSaveYn;
    }

}
