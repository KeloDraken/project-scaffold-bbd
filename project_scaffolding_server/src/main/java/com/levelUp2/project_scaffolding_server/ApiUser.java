package com.levelUp2.project_scaffolding_server;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;;

@RestController
@RequestMapping("/api")
public class ApiUser {

    @GetMapping("/userinfo")
    public ResponseEntity<String> getUserInfo(@RequestParam(value = "access_token") String accessToken) {
        System.out.println("Server: Received token, verifying with Google...");

        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";

        RestTemplate restTemplate = new RestTemplate();
        try {
            String userInfo = restTemplate.getForObject(userInfoUrl + "?access_token=" + accessToken, String.class);
            System.out.println("Server: User details retrieved from Google.");
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or request error.");
        }
    }
}
