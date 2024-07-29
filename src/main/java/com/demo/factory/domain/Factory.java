package com.demo.factory.domain;

import com.demo.factory.dto.factory.FactoryDto;
import com.demo.factory.dto.factory.FactoryDtoForManager;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "factory")
@NoArgsConstructor
@Getter
//@EqualsAndHashCode(callSuper = false)
public class Factory extends BaseDomain{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long factoryNo;

    @NotBlank
    @Column(length = 100, nullable = false, unique = true)
    private String factoryName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FactoryStatusEnum factoryStatus;

    @Setter
    @Column(length = 100)
    private String logoFileName;

    @Setter
    @Column(length = 100)
    private String modelFileName;

    @Transient
    private MultipartFile modelFile;

    @Transient
    private MultipartFile logoFile;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "factory", fetch=FetchType.LAZY)
    private List<Member> memberList = new ArrayList<>();

    public Factory(Long factoryNo) {
        this.factoryNo = factoryNo;
    }

    public Factory(FactoryDto factoryDto) {
        this.factoryNo = null;
        this.factoryName = factoryDto.getFactoryName();
        this.factoryStatus = factoryDto.getFactoryStatus();
        this.logoFileName = factoryDto.getLogoFileImgName();

    }

    public Factory(FactoryDtoForManager factoryDtoForManager) {
        String dtoFactoryNo = factoryDtoForManager.getFactoryNo();
        if (StringUtils.hasText(dtoFactoryNo)) {
            this.factoryNo = Long.valueOf(factoryDtoForManager.getFactoryNo());
        } else {
            this.factoryNo = null;
        }
        this.factoryName = factoryDtoForManager.getFactoryName();
        this.factoryStatus = factoryDtoForManager.getFactoryStatus();
        this.logoFileName = factoryDtoForManager.getLogoFileImgName();
    }

    public Factory modify(Factory factory, FactoryDto factoryDto) {
        String dtoFactoryName = factoryDto.getFactoryName();
        if (StringUtils.hasText(dtoFactoryName)) {
            factory.factoryName = dtoFactoryName;
        }
        factory.factoryStatus = factoryDto.getFactoryStatus();
        return factory;
    }

    public Factory modify(Factory factory, FactoryDtoForManager factoryDto) {
        String dtoFactoryName = factoryDto.getFactoryName();
        if (StringUtils.hasText(dtoFactoryName)) {
            factory.factoryName = dtoFactoryName;
        }
        factory.factoryStatus = factoryDto.getFactoryStatus();
        return factory;
    }
}
