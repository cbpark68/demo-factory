package com.demo.factory.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberAuthEnum {
    ROLE_ADMIN("시스템관리자"),
    ROLE_FACTORY_MANAGER("사이트관리자"),
    ROLE_FACTORY_USER("사용자");

    private final String displayValue;
}
