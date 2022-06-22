package com.hellparty.mapper;

import com.hellparty.domain.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    /* 댓글 갯수 */
    public int commentCount(int boardIdx);

    /* 댓글 리스트 */
    public List<CommentDTO> commentList(int boardId);

    /* 댓글 등록 */
    public int commentRegister(CommentDTO commentDTO);

    /* 댓글 삭제 */
    public int commentDelete(int commentIdx);

}
