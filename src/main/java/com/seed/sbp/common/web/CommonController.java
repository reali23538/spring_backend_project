package com.seed.sbp.common.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {

    @Value("${spring.config.activate.on-profile}")
    private String profileName;

    @GetMapping("/profile-check")
    public ResponseEntity<String> getProfiles() {
        return ResponseEntity.ok(profileName);
    }

}
