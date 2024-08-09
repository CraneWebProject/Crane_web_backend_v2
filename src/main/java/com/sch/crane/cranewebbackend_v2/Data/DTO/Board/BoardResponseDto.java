package com.sch.crane.cranewebbackend_v2.Data.DTO.Board;

import com.sch.crane.cranewebbackend_v2.Domain.Enums.BoardCategory;
import lombok.Builder;
import lombok.Data;

@Data
public class BoardResponseDto {
    private Long bid;

    private String boardTitle;

    private String boardContents;

    private Integer boardView;

    private BoardCategory boardCategory;

    private Long uid;

    private String userName;

//    private String userPic;



    @Builder
    public BoardResponseDto(Long bid, String boardTitle, String boardContents, Integer boardView,
                            BoardCategory boardCategory, Long uid, String userName){
        this.bid = bid;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.boardView = boardView;
        this.boardCategory = boardCategory;
        this.uid = uid;
        this.userName = userName;
//        this.userPic = userPic;
    }
}
