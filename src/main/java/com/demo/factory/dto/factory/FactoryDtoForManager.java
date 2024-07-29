package com.demo.factory.dto.factory;

import com.demo.factory.domain.Factory;
import com.demo.factory.domain.Member;
import com.demo.factory.domain.FactoryStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class FactoryDtoForManager {
    private String factoryNo;

    private String factoryName;

    @Enumerated(EnumType.STRING)
    private FactoryStatusEnum factoryStatus;

    @JsonProperty("logoFileName")
    private String logoFileImgName;

    private String createDatetime;

    private String lastModifyDatetime;

    private String userId;

    private String userPw;

    private String userName;

    public FactoryDtoForManager(Factory factory, Member member) throws Exception {
        this.factoryNo = String.valueOf(factory.getFactoryNo());
        this.factoryName = factory.getFactoryName();
        this.factoryStatus = factory.getFactoryStatus();
        this.logoFileImgName = factory.getLogoFileName();
        this.createDatetime = String.valueOf(factory.getCreateDatetime());
        this.lastModifyDatetime = String.valueOf(factory.getLastModifyDatetime());
        this.userId = member.getUserId();
        this.userPw = member.getUserPw();
        this.userName = member.getUserName();
    }
}
