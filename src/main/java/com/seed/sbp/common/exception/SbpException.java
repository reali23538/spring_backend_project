package com.seed.sbp.common.exception;

import com.seed.sbp.common.response.CommonResultCode;
import lombok.Getter;

@Getter
public class SbpException extends Exception {
    private CommonResultCode resultCode;

    public SbpException(CommonResultCode commonResultCode) {
        super(commonResultCode.getMessage());
        resultCode = commonResultCode;
    }
}
