package com.seed.sbp.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.seed.sbp.auth.domain.OAuth2Dto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuth2LoginService {

    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();

    public void login(String authCode, String oAuth2Company) {
        String accessToken = getAccessToken(authCode, oAuth2Company);
        OAuth2Dto.UserInfo userInfo = getUserInfo(accessToken, oAuth2Company);

        // todo oAuth2 로그인 후처리
        // 등록된 유저인지 확인 > 없으면 유저 등록
        // email(필), password(선), oAuthCompany(선), name...
        // jwt 자체 토큰 생성 및 refreshToken 저장 (accessToken + refreshToken)
    }

    private String getAccessToken(String authCode, String oAuth2Company) {
        String clientId = env.getProperty("oauth2." + oAuth2Company + ".client-id");
        String clientSecret = env.getProperty("oauth2." + oAuth2Company + ".client-secret");
        String redirectUri = env.getProperty("oauth2." + oAuth2Company + ".redirect-uri");
        String authServerUri = env.getProperty("oauth2." + oAuth2Company + ".authorization-server-uri");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity httpEntity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(authServerUri, HttpMethod.POST, httpEntity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        System.out.println("accessTokenNode >>> " + accessTokenNode);
        return accessTokenNode.get("access_token").asText();
    }

    private OAuth2Dto.UserInfo getUserInfo(String accessToken, String oAuthCompany) {
        String resourceServerUri = env.getProperty("oauth2." + oAuthCompany + ".resource-server-uri");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(resourceServerUri, HttpMethod.GET, httpEntity, JsonNode.class);
        JsonNode userResourceNode = responseNode.getBody();
        System.out.println("userResourceNode >>> " + userResourceNode);

        return OAuth2Dto.UserInfo.builder()
                .id(userResourceNode.get("id").asText())
                .email(userResourceNode.get("email").asText())
                .nickName(userResourceNode.get("name").asText())
                .build();
    }

}
