package com.atharva.auth.core.model;

public enum ErrorCodes {
    SUCCESS(200),

    PASS_INCORRECT(401),
    ID_INCORRECT(402),
    ID_ALREADY_EXITS(403),
    AUTH_KEY_NOT_VALID(405),
    ACCOUNT_NOT_VERIFIED(406),
    ID_NOT_SAME(407),

    PROJECT_ID_INCORRECT(410),
    PROJECT_PASS_INCORRECT(411),

    UNKNOWN(500);

    int code;
    ErrorCodes(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
