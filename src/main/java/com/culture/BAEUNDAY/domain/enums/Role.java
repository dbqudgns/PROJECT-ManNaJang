package com.culture.BAEUNDAY.domain.enums;

import lombok.Getter;

public enum Role {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    Role(String value) {
        this.value = value;
    }

    @Getter
    private String value;

}
