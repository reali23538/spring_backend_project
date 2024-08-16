package com.seed.sbp.common.file.domain;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
public class FileInfo {
    private String path;
    private String originalFileName; // 원본 파일명
    private String uploadedFileName; // 저장 파일명
    private Long fileSize;           // 파일 크기

    @Builder
    public FileInfo(String path, String originalFileName, String uploadedFileName, Long fileSize) {
        this.path = path;
        this.originalFileName = originalFileName;
        this.uploadedFileName = uploadedFileName;
        this.fileSize = fileSize;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
