package com.hellparty.service;

import com.hellparty.domain.UserDTO;
import com.hellparty.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void userJoin(UserDTO userVO) throws Exception {
        log.info("Service userJoin.........");
        userMapper.userJoin(userVO);
    }

    @Override
    public int idCheck(String userId) throws Exception {
        log.info("Service idCheck...........");
        return userMapper.idCheck(userId);
    }

    @Override
    public Map<String, String> validateHandler(Errors errors) {
        Map<String, String> validateResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = "valid_" + error.getField();
            validateResult.put(validKeyName, error.getDefaultMessage());
        }

        return validateResult;
    }

    @Override
    public UserDTO userLogin(UserDTO userDTO){
        return userMapper.userLogin(userDTO);
    }
}
