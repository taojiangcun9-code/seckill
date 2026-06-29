package org.example.service;

import org.example.dto.LoginParam;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.result.CodeMsg;
import org.example.result.Result;
import org.example.common.RedisKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TokenService tokenService;

    public Result<String> doLogin(LoginParam loginParam) {

        User user = userMapper.selectByPhone(loginParam.getMobile());

        if (user == null) {
            return Result.error(CodeMsg.ERROR);
        }

        if (user==null||!user.getPassword().equals(loginParam.getPassword())) {
            return Result.error(CodeMsg.ERROR);
        }

        String token = tokenService.createToken(user.getId());

        redisService.set(
                RedisKeys.tokenKey(token),
                user.getId(),
                3600 * 24
        );

        return Result.success(token);
    }
}