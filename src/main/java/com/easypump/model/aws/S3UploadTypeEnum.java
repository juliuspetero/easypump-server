package com.easypump.model.aws;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum S3UploadTypeEnum {
    BODA_BODA_HEAD_SHOT("bodaboda_headshot"),
    MOBILE_MONEY("mobile_money"),
    CHEQUE("cheque");
    private final String folder;
}
