package com.hellparty.service;

import com.hellparty.domain.CommentDTO;

import java.util.List;

public interface CommentService {

    /* 댓글 갯수 */
    public int commentCount(int boardIdx);

    /* 댓글 리스트 */
    public List<CommentDTO> commentList(int boardId);

    /* 댓글 등록 */
    public int commentRegister(CommentDTO commentDTO);

    /* 댓글 삭제 */
    public int commentDelete(int commentIdx);


}
