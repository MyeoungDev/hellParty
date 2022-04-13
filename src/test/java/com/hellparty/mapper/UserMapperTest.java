package com.hellparty.mapper;

import com.hellparty.domain.UserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

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

    @Test
    public void test() {
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int num = random.nextInt(888888) + 111111;
            System.out.println("random = " + num);
        }

    }


}