package com.demo.factory.domain;

import com.demo.factory.dto.member.MemberDto;
import com.demo.factory.dto.member.MemberDtoForFactoryManager;
import com.demo.factory.dto.factory.FactoryDtoForManager;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="dt_user")
@NoArgsConstructor
@Getter
//@EqualsAndHashCode(of="userNo", callSuper = false)
public class Member extends BaseDomain{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userNo;
	
	@NotBlank
	@Column(length = 20, nullable = false, unique = true)
	private String userId;
	
	@NotBlank
	@Column(length = 200, nullable = false)
	private String userPw;

	@NotBlank
	@Column(length = 100, nullable = false)
	private String userName;

	@Column(length = 50)
	private String defaultDashboardNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "factory_no")
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	private Factory factory;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "user_no")
	private List<MemberAuth> authList = new ArrayList<>();

	//하나의 유저는 하나의 권한만 가질수 있도록 한다.
	public void setMemberAuth(MemberAuth auth) {
		authList.clear();
		authList.add(auth);
	}

	public Member(Long userNo) {
		this.userNo = userNo;
	}

	public Member(MemberDto memberDto) {
		this.userNo = null;
		this.userId = memberDto.getUserId();
		this.userPw = memberDto.getUserPw();
		this.userName = memberDto.getUserName();
		this.defaultDashboardNo = memberDto.getDefaultDashboardNo();
		this.factory = new Factory(Long.valueOf(memberDto.getFactory().getFactoryNo()));
		setMemberAuth(new MemberAuth(MemberAuthEnum.ROLE_SITE_USER));
	}

	public Member(FactoryDtoForManager siteDtoForManager) {
		this.userId = siteDtoForManager.getUserId();
		this.userPw = siteDtoForManager.getUserPw();
		this.userName = siteDtoForManager.getUserName();
		this.factory = new Factory(Long.valueOf(siteDtoForManager.getFactoryNo()));
		setMemberAuth(new MemberAuth(MemberAuthEnum.ROLE_SITE_MANAGER));
	}

	public Member modify(Member member, MemberDto memberDto) {
		String dtoUserPw = memberDto.getUserPw();
		String dtoUserName = memberDto.getUserName();
		String dtoDefaultDashboardNo = memberDto.getDefaultDashboardNo();
		if (StringUtils.hasText(dtoUserPw)) {
			member.userPw = dtoUserPw;
		}
		if (StringUtils.hasText(dtoUserName)) {
			member.userName = dtoUserName;
		}
		if (StringUtils.hasText(dtoDefaultDashboardNo)) {
			member.defaultDashboardNo = dtoDefaultDashboardNo;
		}
		return member;
	}

	public Member modify(Member member, MemberDtoForFactoryManager memberDto) {
		String dtoUserPw = memberDto.getUserPw();
		String dtoUserName = memberDto.getUserName();
		if (StringUtils.hasText(dtoUserPw)) {
			member.userPw = dtoUserPw;
		}
		if (StringUtils.hasText(dtoUserName)) {
			member.userName = dtoUserName;
		}
		member.defaultDashboardNo = memberDto.getDefaultDashboardNo();
		return member;
	}
}
