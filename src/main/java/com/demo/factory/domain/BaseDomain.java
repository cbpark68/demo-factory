package com.demo.factory.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
public abstract class BaseDomain extends BaseDateTimeEntity {

    @CreatedBy
    @Column(length = 20, updatable = false)
    private String createUser;

    @LastModifiedBy
    @Column(length = 20)
    private String lastModifyUser;

}