package com.hellparty.service;

import com.hellparty.domain.UserDTO;
import org.springframework.validation.Errors;

import java.util.Map;

public interface UserService {

    /* 회원가입 */
    public void userJoin(UserDTO userVO) throws Exception;

    /* 아이디 중복 체크 */
    public int idCheck(String userId) throws Exception;

    /* 회원가입 Validation */
    public Map<String, String> validateHandler(Errors errors);


}
