package com.demo.factory.common.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of="logNo")
@ToString
@Entity
@Table(name="access_log")
public class AccessLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long logNo;
	
	@Column(length = 200, nullable = false)
	private String requestUri;
	
	@Column(length = 100, nullable = false)
	private String className;
	
	@Column(length = 50, nullable = false)
	private String classSimpleName;
	
	@Column(length = 100, nullable = false)
	private String methodName;

	@Column(length = 50, nullable = false)
	private String remoteAddr;

	@Column(length = 50)
	private String username;
	
	@CreationTimestamp
	private LocalDateTime regDate;
	@UpdateTimestamp
	private LocalDateTime updDate;

}
