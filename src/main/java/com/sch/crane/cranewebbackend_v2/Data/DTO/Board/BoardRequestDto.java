package com.sch.crane.cranewebbackend_v2.Data.DTO.Board;

import com.sch.crane.cranewebbackend_v2.Data.DTO.Comment.CommentResponseDto;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Comment;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.BoardCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class BoardRequestDto {
    private String boardTitle;

    private String boardContents;

    private BoardCategory boardCategory;


    @Builder
    public BoardRequestDto(String boardTitle, String boardContents, BoardCategory boardCategory){
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.boardCategory = boardCategory;
    }
}
