package com.sch.crane.cranewebbackend_v2.Data.DTO.Board;

import com.sch.crane.cranewebbackend_v2.Data.DTO.User.UserResponseDto;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Board;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.BoardCategory;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardResponseDto {
    private Long bid;

    private String boardTitle;

    private String boardContents;

    private Integer boardView;

    private BoardCategory boardCategory;

    private UserResponseDto userResponseDto;

    private String thumbNaile;

    private LocalDateTime createdDate;

    @Builder
    public BoardResponseDto(Long bid, String boardTitle, String boardContents, Integer boardView,
                            BoardCategory boardCategory,UserResponseDto userResponseDto, String thumbNaile , LocalDateTime createdDate){
        this.bid = bid;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.boardView = boardView;
        this.boardCategory = boardCategory;
        this.userResponseDto = userResponseDto;
        this.thumbNaile = thumbNaile;
        this.createdDate = createdDate;
    }

    public BoardResponseDto(Board board){
        this.bid = board.getBid();
        this.boardTitle = board.getBoardTitle();
        this.boardContents = board.getBoardContents();
        this.boardView = board.getBoardView();
        this.boardCategory = board.getBoardCategory();
        this.userResponseDto = new UserResponseDto(board.getUser());
//        this.thumbNaile =
        this.createdDate = board.getCreatedDate();
    }
}
