package com.seed.sbp.common.exception;

import com.seed.sbp.common.response.CommonResultCode;

public class NoContentException extends Exception {
    public NoContentException(CommonResultCode commonResultCode) {
        super(commonResultCode.getMessage());
    }
}
