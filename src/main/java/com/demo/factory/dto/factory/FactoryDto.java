package com.demo.factory.dto.factory;

import com.demo.factory.domain.Factory;
import com.demo.factory.domain.FactoryStatusEnum;
import com.demo.factory.domain.Member;
import com.demo.factory.domain.MemberAuthEnum;
import com.demo.factory.dto.member.MemberDto;
import com.demo.factory.dto.member.MemberDtoForFactoryManager;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@ToString
public class FactoryDto {
    private String factoryNo;

    private String factoryName;

    @Enumerated(EnumType.STRING)
    private FactoryStatusEnum factoryStatus;

    @JsonProperty("logoFileName")
    private String logoFileImgName;

    private String modelFileName;

    private String createDatetime;

    private String lastModifyDatetime;

    @Transient
    private MultipartFile modelFile;

    @Transient
    private MultipartFile logoFile;

    private MemberDtoForFactoryManager factoryManager;

    private List<MemberDto> memberDtoList = new ArrayList<>();

    public FactoryDto(Factory factory) throws Exception {
        this.factoryNo = String.valueOf(factory.getFactoryNo());
        this.factoryName = factory.getFactoryName();
        this.factoryStatus = factory.getFactoryStatus();
        this.logoFileImgName = factory.getLogoFileName();
        this.createDatetime = String.valueOf(factory.getCreateDatetime());
        this.lastModifyDatetime = String.valueOf(factory.getLastModifyDatetime());
        List<Member> memberList = factory.getMemberList();
        for (Member member : memberList) {
            this.memberDtoList.add(new MemberDto(member));
            if (member.getAuthList().get(0).getAuth().equals(MemberAuthEnum.ROLE_FACTORY_MANAGER)) {
                this.factoryManager = new MemberDtoForFactoryManager(member);
            }
        }
    }

}
