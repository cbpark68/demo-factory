package com.demo.factory.domain;

import com.demo.factory.dto.facilityCode.FacilityCodeDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "facility_code")
@NoArgsConstructor
@Getter
public class FacilityCode extends BaseDomain{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_code_no")
    private Long facilityCodeNo;

    @NotBlank
    @Column(name = "facility_code", length = 20,nullable = false,unique = true)
    private String facilityCode;

    @NotBlank
    @Column(name = "facility_code_name", length = 200,nullable = false)
    private String facilityCodeName;

    @Column(name = "facility_code_info", columnDefinition = "LONGTEXT")
    private String facilityCodeInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factory_no")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Factory factory;

    public FacilityCode(Long facilityCodeNo) {
        this.facilityCodeNo = facilityCodeNo;
    }

    public FacilityCode(FacilityCodeDto facilityCodeDto) {
        this.facilityCodeNo = null;
        this.facilityCode = facilityCodeDto.getFacilityCode();
        this.facilityCodeName = facilityCodeDto.getFacilityCodeName();
        this.facilityCodeInfo = facilityCodeDto.getFacilityCodeInfo();
        this.factory = new Factory(Long.valueOf(facilityCodeDto.getFactoryNo()));
    }

    public FacilityCode modify(FacilityCode facilityCode, FacilityCodeDto facilityCodeDto) {
        String dtoFacilityCode = facilityCodeDto.getFacilityCode();
        String dtoFacilityCodeName = facilityCodeDto.getFacilityCodeName();
        if (StringUtils.hasText(dtoFacilityCode)) {
            facilityCode.facilityCode = dtoFacilityCode;
        }
        if (StringUtils.hasText(dtoFacilityCodeName)) {
            facilityCode.facilityCodeName = dtoFacilityCodeName;
        }
        facilityCode.facilityCodeInfo = facilityCodeDto.getFacilityCodeInfo();
        return facilityCode;
    }


}
