package org.example.controller;

import org.example.dto.LoginParam;
import org.example.result.CodeMsg;
import org.example.result.Result;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userservice;

    @PostMapping("/dologin")
    public Result<String> doLogin(@RequestBody LoginParam loginParam) {
        return userservice.doLogin(loginParam);
    }
}
