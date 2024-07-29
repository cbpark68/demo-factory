package com.demo.factory.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DataSaveEnum {
    Y("저장가능"),
    N("저장불가");

    private final String displayValue;
}
