package com.demo.factory.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name="user_auth")
@NoArgsConstructor
public class MemberAuth extends BaseDomain{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userAuthNo;

	@Column(name = "user_no")
	private Long userNo;


	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MemberAuthEnum auth;

	public MemberAuth(MemberAuthEnum auth) {
		this.auth = auth;
	}

}
