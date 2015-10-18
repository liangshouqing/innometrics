package com.kyleong.innometrics.utils;

/**
 * Created by SQL on 2015/10/18.
 */
public enum RestReturnCode {
    SUCCESS(0, "SUCCESS."),
    COUNTER_NOT_FOUND(1, "COUNTER NOT FOUND");

    private final int code;
    private final String description;

    private RestReturnCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
