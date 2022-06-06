package com.hellparty.mapper;

import com.hellparty.domain.BoardDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {

    /* 글 등록 */
    public void boardRegister(BoardDTO boardDTO);

    /* detail view */
    public BoardDTO boardDetail(int boardIdx);

}
