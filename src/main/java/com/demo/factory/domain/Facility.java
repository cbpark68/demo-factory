package com.demo.factory.domain;

import com.demo.factory.dto.facility.FacilityDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.StringUtils;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "dt_facility",
        uniqueConstraints={
                @UniqueConstraint(
                        name="dt_facility_const1",
                        columnNames={"factory_no", "facility_name"}
                )
        })
public class Facility extends BaseDomain{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_no")
    private Long facilityNo;

    @NotBlank
    @Column(name = "facility_name",nullable = false, length = 20)
    private String facilityName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_code_no")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private FacilityCode facilityCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factory_no")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Factory factory;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_save_yn", nullable = false)
    private DataSaveEnum dataSaveYn;

    public Facility(Long facilityNo) {
        this.facilityNo = facilityNo;
    }

    public Facility(FacilityDto facilityDto) {
        this.facilityNo = null;
        this.facilityName = facilityDto.getFacilityName();
        this.facilityCode = new FacilityCode(Long.valueOf(facilityDto.getFacilityCodeNo()));
        this.factory = new Factory(Long.valueOf(facilityDto.getFactoryNo()));
        this.dataSaveYn = DataSaveEnum.valueOf(facilityDto.getDataSaveYn());
    }

    public Facility modify(Facility facility, FacilityDto facilityDto) {
        String dtoFacilityName = facilityDto.getFacilityName();
        if (StringUtils.hasText(dtoFacilityName)) {
            facility.facilityName = dtoFacilityName;
        }
        facility.dataSaveYn = DataSaveEnum.valueOf(facilityDto.getDataSaveYn());
        return facility;
    }

}
