package com.hellparty.mapper;

import com.hellparty.domain.BoardDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {

    /* 글 등록 */
    public void boardRegister(BoardDTO boardDTO);

    /* detail view */
    public BoardDTO boardDetail(int boardIdx);

    /* 조회수 증가 */
    public void boardViewCount(int boardIdx);

    /* 클릭 했을때 조회를 해서 return 0 이면 없으니 추가, return 1이면 있는거니 삭제 */

    /* 하트 조회 */
    public boolean heartFind(int userIdx, int boardIdx);

    /* 하트 클릭 추가 */
    public void heartInsert(int userIdx, int boardIdx);

    /* 하트 클릭 삭제 */
    public void heartDelete(int userIdx, int boardIdx);


}
