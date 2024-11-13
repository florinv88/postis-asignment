package com.fnkcode.postis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum RequestStatuses {
    APPROVED("APPROVED","1"),
    PENDING("PENDING","0"),
    REJECTED("REJECTED","-1");

    private final String name;
    private final String value;

    private static final Map<String, RequestStatuses> STATUS_MAP = new HashMap<>();

    static {
        for (RequestStatuses status : RequestStatuses.values()) {
            STATUS_MAP.put(status.getName(), status);
        }
    }

    public static RequestStatuses getStatus(String status) {
        return STATUS_MAP.get(status);
    }
}
