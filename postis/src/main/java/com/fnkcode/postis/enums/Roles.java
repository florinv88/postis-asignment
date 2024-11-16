package com.fnkcode.postis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Roles {

    MANAGER("manager"),
    EMPLOYEE("engineer");

    private final String name;


}
