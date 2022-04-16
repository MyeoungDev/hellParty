package com.hellparty.mapper;

import com.hellparty.domain.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    /* 회원가입 */
    public void userJoin(UserDTO userDTO);

    /* 아이디 중복 체크  return 1: 중복 O, 0: 중복 X*/
    public int idCheck(String userId);

}
