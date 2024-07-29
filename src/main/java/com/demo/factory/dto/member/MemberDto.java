package com.demo.factory.dto.member;

import com.demo.factory.domain.Member;
import com.demo.factory.domain.MemberAuthEnum;
import com.demo.factory.dto.factory.FactoryDtoForLogin;
import com.demo.factory.dto.factory.FactoryDtoForManager;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberDto {
    private String userNo;
    private String userId;
    private String userPw;
    private String userName;
    private String defaultDashboardNo;
    private MemberAuthEnum auth;
    private FactoryDtoForLogin factory;
    private String createDatetime;
    private String lastModifyDatetime;

    public MemberDto(Member member) {
        this.userNo = String.valueOf(member.getUserNo());
        this.userId = member.getUserId();
        this.userPw = member.getUserPw();
        this.userName = member.getUserName();
        this.defaultDashboardNo = member.getDefaultDashboardNo();
        this.auth = member.getAuthList().get(0).getAuth();
        this.factory = new FactoryDtoForLogin(member.getFactory().getFactoryNo());
        this.createDatetime = String.valueOf(member.getCreateDatetime());
        this.lastModifyDatetime = String.valueOf(member.getLastModifyDatetime());
    }

    public MemberDto(Member member, FactoryDtoForLogin factoryDtoForLogin) {
        this.userNo = String.valueOf(member.getUserNo());
        this.userId = member.getUserId();
        this.userPw = member.getUserPw();
        this.userName = member.getUserName();
        this.defaultDashboardNo = member.getDefaultDashboardNo();
        this.auth = member.getAuthList().get(0).getAuth();
        this.factory = factoryDtoForLogin;
        this.createDatetime = String.valueOf(member.getCreateDatetime());
        this.lastModifyDatetime = String.valueOf(member.getLastModifyDatetime());
    }

    public MemberDto(FactoryDtoForManager factoryDtoForManager) {
        this.userId = factoryDtoForManager.getUserId();
        this.userPw = factoryDtoForManager.getUserPw();
        this.userName = factoryDtoForManager.getUserName();
    }
}
