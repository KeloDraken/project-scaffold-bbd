package com.levelUpTwo.project_scaffolding;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public OAuth2AuthenticationSuccessHandler(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
                                            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName()
        );

        if (authorizedClient == null) {
            throw new RuntimeException("OAuth2AuthorizedClient not found");
        }
        
        String authorizationCode = request.getParameter("code");
        if (authorizationCode == null || authorizationCode.isEmpty()) {
            throw new RuntimeException("Authorization code not found in request");
        }
        
        // Exchange authorization code for JWT
        String jwt = exchangeAuthorizationCodeForJWT(authorizationCode);
        
        // Store JWT in session
        request.getSession().setAttribute("JWT", jwt);
        
        // Redirect to home page after login
        response.sendRedirect("/home");
    }
    
    private String exchangeAuthorizationCodeForJWT(String authorizationCode) {
        System.out.println(authorizationCode);
        String tokenEndpoint = "https://www.googleapis.com/oauth2/v4/token";
        String redirectUri = "http://localhost:8000/login/oauth2/code/google";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(tokenEndpoint, request, String.class);

        System.out.println("Response: " + response.getBody());
        return response.getBody().toString();
    }
}