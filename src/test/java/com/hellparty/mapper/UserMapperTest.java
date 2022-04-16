package com.hellparty.mapper;

import com.hellparty.domain.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void userJoinTest() {
        UserDTO user = new UserDTO();
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

    @Test
    public void test2() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key01", "value01");
        map.put("key02", "value02");
        map.put("key03", "value03");
        map.put("key04", "value04");
        map.put("key05", "value05");

        // 방법 02 : keySet()
        for (String key : map.keySet()) {
            String value = map.get(key);
            System.out.println("[key]:" + key + ", [value]:" + value);
        }

        System.out.println("map = " + map);
        System.out.println("map.keySet() = " + map.keySet());
    }


}