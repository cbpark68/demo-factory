package com.demo.factory.dto.member;

import com.demo.factory.domain.Member;
import com.demo.factory.domain.MemberAuthEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MemberDtoForFactoryManager {
    private String userNo;
    private String userId;
    private String userPw;
    private String userName;
    private String defaultDashboardNo;
    private MemberAuthEnum auth;
    private String siteNo;
    private String createDatetime;
    private String lastModifyDatetime;

    public MemberDtoForFactoryManager(Member member) throws Exception {
        this.userNo = String.valueOf(member.getUserNo());
        this.userId = member.getUserId();
        this.userPw = member.getUserPw();
        this.userName = member.getUserName();
        this.defaultDashboardNo = member.getDefaultDashboardNo();
        this.auth = member.getAuthList().get(0).getAuth();
        this.siteNo = String.valueOf(member.getFactory().getFactoryNo());
        this.createDatetime = String.valueOf(member.getCreateDatetime());
        this.lastModifyDatetime = String.valueOf(member.getLastModifyDatetime());
    }
}
