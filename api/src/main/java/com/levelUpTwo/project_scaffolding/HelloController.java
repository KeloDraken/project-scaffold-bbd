package com.levelUpTwo.project_scaffolding;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("protected")
    public String greet(){
        return "Hello World!!";
    }
}
