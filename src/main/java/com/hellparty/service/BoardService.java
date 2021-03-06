package com.hellparty.service;

import com.hellparty.domain.BoardDTO;

public interface BoardService {

    /* 글 등록 */
    public void boardRegister(BoardDTO boardDTO);

    /* 글 조회 */
    public BoardDTO boardDetail(int boardIdx);

    /* 조회수 증가 */
    public void boardViewCount(int boardIdx);

}
