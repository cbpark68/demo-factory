package com.demo.factory.dto.member;

import com.demo.factory.domain.Member;
import com.demo.factory.domain.MemberAuthEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDtoForList {
    private String userNo;
    private String userId;
    private String userPw;
    private String userName;
    private MemberAuthEnum auth;
    private String defaultDashboardNo;
    private String createDatetime;
    private String lastModifyDatetime;

    public MemberDtoForList(Member member) {
        this.userNo = String.valueOf(member.getUserNo());
        this.userId = member.getUserId();
        this.userPw = member.getUserPw();
        this.userName = member.getUserName();
        this.defaultDashboardNo = member.getDefaultDashboardNo();
        this.auth = member.getAuthList().get(0).getAuth();
        this.createDatetime = String.valueOf(member.getCreateDatetime());
        this.lastModifyDatetime = String.valueOf(member.getLastModifyDatetime());
    }
}
