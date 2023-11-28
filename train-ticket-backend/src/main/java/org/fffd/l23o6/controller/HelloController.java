package org.fffd.l23o6.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, l23o6!";
    }
}