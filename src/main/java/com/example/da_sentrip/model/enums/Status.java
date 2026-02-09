package com.example.da_sentrip.model.enums;

import lombok.Getter;

public enum Status {
    ACTIVE(1,"Hoạt động "),
    INACTIVE (0," Không hoạt động ");
    private final int value;
    @Getter
    private final String name;
    Status(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
