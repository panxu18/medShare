package com.xp.medshare.model.vomodel;

public enum  ResponseCode {
    SUCCESS(0, "success"),
    UNKNOWN(1, "serer error"),
    PARAM_INVALID(2, "parameter invalid"),
    CONTRACT_ERROR(3, "contract error"),
    EVIDENCE_EXISTED(10001, "evidence id existed"),
    ;

    int code;
    String msg;
    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
