package com.hellparty.service;

import com.hellparty.domain.UserVO;
import com.hellparty.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void userJoin(UserVO userVO) throws Exception {
        log.info("Service userJoin.........");
        userMapper.userJoin(userVO);
    }

    @Override
    public int idCheck(String userId) throws Exception {
        log.info("Service idCheck...........");
        return userMapper.idCheck(userId);
    }
}
