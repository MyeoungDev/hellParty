package com.hellparty.mapper;

import com.hellparty.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    /* 회원가입 */
    public void userJoin(UserVO userVO);

}
