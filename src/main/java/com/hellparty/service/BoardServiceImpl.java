package com.hellparty.service;

import com.hellparty.domain.BoardDTO;
import com.hellparty.mapper.BoardMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardMapper boardMapper;

    @Override
    public void boardRegister(BoardDTO boardDTO) {
        log.info("Service boardRegister");
        boardMapper.boardRegister(boardDTO);
    }

    @Override
    public BoardDTO boardDetail(int boardIdx) {
        log.info("Service boardDetail");
        return boardMapper.boardDetail(boardIdx);
    }

    @Override
    public void boardViewCount(int boardIdx) {
        log.info("Service boardViewCount");
        boardMapper.boardViewCount(boardIdx);
    }
}

