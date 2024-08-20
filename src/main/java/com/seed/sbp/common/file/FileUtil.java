package com.seed.sbp.common.file;

import com.seed.sbp.common.file.domain.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FileUtil {
    // todo : 다중 파일 업로드 실패시, 모두 삭제 or 실패한 파일의 사유를 리턴하도록 개선
    @Value("${file.upload-root-path}")
    private String UPLOAD_ROOT_PATH;
    @Value("${file.max-file-size}")
    private Long MAX_FILE_SIZE;
    @Value("${file.impossible-types}")
    private List<String> IMPOSSIBLE_TYPES;

    public List<FileInfo> upload(String path, final List<MultipartFile> files) {
        List<FileInfo> fileInfos = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            FileInfo fileInfo = upload(path, file);
            if (fileInfo != null) fileInfos.add(fileInfo);
            log.debug("fileInfo : {}", fileInfo);
        }
        return fileInfos;
    }

    public FileInfo upload(String path, final MultipartFile file) {
        if (!validate(file)) return null;

        String uniqueFileName = getUniqueFileName(file.getOriginalFilename());
        String uploadPath = getUploadPath(path);
        String uploadFile = uploadPath + File.separator + uniqueFileName;
        File uploadedFile = new File(uploadFile);
        log.debug("uploadFile : {}", uploadFile);

        try {
            file.transferTo(uploadedFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return FileInfo.builder()
                .path(uploadPath)
                .originalFileName(file.getOriginalFilename())
                .uploadedFileName(uniqueFileName)
                .fileSize(file.getSize())
                .build();
    }

    private boolean validate(MultipartFile file) {
        if (file.isEmpty()) return false;
        if (file.getSize() > MAX_FILE_SIZE) return false; // 파일 용량 체크
        if (isImpossibleType(file)) return false; // 파일 타입 체크
        return true;
    }
    private boolean isImpossibleType(MultipartFile file) {
        log.debug("contentType : {}", file.getContentType());
        if (IMPOSSIBLE_TYPES == null) return false;
        if (IMPOSSIBLE_TYPES.size() < 1) return false;

        return IMPOSSIBLE_TYPES.contains(file.getContentType());
    }

    private String getUniqueFileName(final String fileName) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = this.getExtension(fileName);
        return uuid + "." + extension;
    }
    public String getExtension(String fileName) {
        if (StringUtils.isEmpty(fileName)) return null;
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String getUploadPath(String path) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String dir = UPLOAD_ROOT_PATH + File.separator + today;
        if (StringUtils.isNotBlank(path)) {
            dir = UPLOAD_ROOT_PATH + File.separator + path + File.separator + today;
        }
        return makeDirectories(dir);
    }
    private String makeDirectories(final String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    public ResponseEntity<Resource> download(String path, String fileName) throws MalformedURLException, UnsupportedEncodingException {
        String filePath = UPLOAD_ROOT_PATH + File.separator + path + File.separator + fileName;
        Resource resource = new UrlResource(Paths.get(filePath).toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"")
                .body(resource);
    }

}
