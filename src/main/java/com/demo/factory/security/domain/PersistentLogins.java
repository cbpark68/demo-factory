package com.demo.factory.security.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "series")
@Entity
@Table(name="persistent_logins")
public class PersistentLogins {
	
	@Id
	@Column(length = 64)
	private String series;
	
	@Column(length = 64)
	private String username;
	
	@Column(length = 64)
	private String token;
	
	@CreationTimestamp
	@Column(name = "last_used")
	private Timestamp lastUsed;

}
