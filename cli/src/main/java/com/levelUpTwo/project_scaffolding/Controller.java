package com.levelUpTwo.project_scaffolding;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Controller {

    @GetMapping("login/oauth2/code/google")
    public String getUserInfo(
            @RequestParam String state,
            @RequestParam String code,
            @RequestParam String scope,
            @RequestParam String authuser,
            @RequestParam String prompt) {
        
        // You can print or process the received parameters as needed
        String response = String.format("Login success! State: %s, Code: %s, Scope: %s, Authuser: %s, Prompt: %s",
                state, code, scope, authuser, prompt);

        return response;
    }
    @GetMapping("login/success")
    public String getUserInfo(){
        String response = String.format("Login success!");

        return response;
    }
}