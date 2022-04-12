package com.hellparty.service;

import com.hellparty.domain.UserVO;
import com.hellparty.mapper.UserMapper;

public interface UserService {

    /* 회원가입 */
    public void userJoin(UserVO userVO) throws Exception;

    /* 아이디 중복 체크 */
    public int idCheck(String userId) throws Exception;


}
