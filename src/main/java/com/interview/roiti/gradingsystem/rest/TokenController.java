package com.interview.roiti.gradingsystem.rest;

import com.interview.roiti.gradingsystem.model.User;
import com.interview.roiti.gradingsystem.service.TokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/teacher")
    public String getTeacherToken() {
        return tokenService.generateToken(new User("teacher", -1L, true));
    }

    @GetMapping("/student/{id}")
    public String getStudentToken(@PathVariable Long id) {
        return tokenService.generateToken(new User("student", id, false));
    }

}
