package com.hellparty.service;

import com.hellparty.domain.CommentDTO;
import com.hellparty.mapper.CommentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;


    @Override
    public int commentCount(int boardIdx) {
        log.info("Service commentCount");
        return commentMapper.commentCount(boardIdx);
    }

    @Override
    public int commentRegister(CommentDTO commentDTO) {
        log.info("Service commentRegister");
        return commentMapper.commentRegister(commentDTO);
    }

    @Override
    public List<CommentDTO> commentList(int boardId) {
        log.info("Service commentList");
        List<CommentDTO> commentList = commentMapper.commentList(boardId);

        return commentList;
    }

    @Override
    public int commentDelete(int commentIdx) {
        log.info("Service commentDelete");

        int result = commentMapper.commentDelete(commentIdx);

        return result;
    }
}
