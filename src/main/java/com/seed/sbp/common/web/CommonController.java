package com.seed.sbp.common.web;

import com.seed.sbp.common.file.FileUtil;
import com.seed.sbp.common.file.domain.FileInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@RestController
public class CommonController {

    @Value("${spring.config.activate.on-profile}")
    private String profileName;

    private final FileUtil fileUtil;

    public CommonController(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    @GetMapping("/profile-check")
    public ResponseEntity<String> getProfiles() {
        return ResponseEntity.ok(profileName);
    }

    @Operation(summary = "파일 업로드", description = "파일 업로드 API 입니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestPart("files") List<MultipartFile> files) {
        String path = "sbp";
        List<FileInfo> fileInfos = fileUtil.upload(path, files);
        return "uploaded : " + fileInfos.size();
    }

    @Operation(summary = "파일 다운로드", description = "파일 업로드 API 입니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/files")
    public ResponseEntity<Resource> download() throws MalformedURLException, UnsupportedEncodingException {
        String path = "sbp/20240816";
        String fileName = "2e36bdddb4504a1e881502b2f4b3ffda.png";
        return fileUtil.download(path, fileName);
    }

}
