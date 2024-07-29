package com.demo.factory.security.dto;

import com.demo.factory.domain.Member;
import com.demo.factory.dto.factory.FactoryDtoForLogin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDtoForSecurity {
    private String username;
    private String password;
    private String userNo;
    private String userId;
    private String userPw;
    private String originUserName;
    private List<String> authList;
    private FactoryDtoForLogin factory;
//    private List<DashboardDto> dashboardList;
    private String createDatetime;
    private String lastModifyDatetime;

    public MemberDtoForSecurity(Member member) {
        this.username = member.getUserId(); //시큐리티 필수값
        this.password = member.getUserPw(); //시큐리티 필수값
        this.userId = member.getUserId();
        this.userPw = member.getUserPw();
        this.userNo = String.valueOf(member.getUserNo());
        this.originUserName = member.getUserName();
        this.createDatetime = String.valueOf(member.getCreateDatetime());
        this.lastModifyDatetime = String.valueOf(member.getLastModifyDatetime());

        this.authList = member.getAuthList().stream()
                .map(auth -> String.valueOf(auth.getAuth()))
                .collect(Collectors.toList());
    }
}
