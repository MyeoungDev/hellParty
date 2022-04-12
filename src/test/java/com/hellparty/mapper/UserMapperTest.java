package com.hellparty.mapper;

import com.hellparty.domain.UserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void userJoinTest() {
        UserVO user = new UserVO();
        user.setUserId("test1");
        user.setUserPw("test1");
        user.setUserName("test1");
        user.setUserEmail("test1");

        userMapper.userJoin(user);
    }

    @Test
    public void idCheckTest() {

        String userId = "test1";

        int result = userMapper.idCheck(userId);

        System.out.println("result = " + result);
    }



}