package com.levelUpTwo.project_scaffolding;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/protected")
    public String greet(HttpSession session) {
        String accessToken = (String) session.getAttribute("ACCESS_TOKEN");

        if (accessToken == null || accessToken.isEmpty()) {
            return "Unauthorized: No access token found. Please log in.";
        }

        return "Hello World!! You are authenticated with token: " + accessToken;
    }
    
    @GetMapping("/userinfo")
    public String getUserInfo(HttpSession session) {
        String accessToken = (String) session.getAttribute("ACCESS_TOKEN");

        if (accessToken == null || accessToken.isEmpty()) {
            return "Unauthorized: No access token found. Please log in.";
        }

        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
        RestTemplate restTemplate = new RestTemplate();
        String userInfo = restTemplate.getForObject(userInfoUrl + "?access_token=" + accessToken, String.class);

        return "User Info: " + userInfo;
    }
}