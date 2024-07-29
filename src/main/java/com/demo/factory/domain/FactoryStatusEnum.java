package com.demo.factory.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FactoryStatusEnum {
    STANDBY("스탠바이"),
    ACTIVE("액티브");

    private final String displayValue;
}
