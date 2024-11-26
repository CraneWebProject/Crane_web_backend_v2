package com.sch.crane.cranewebbackend_v2.Data.DTO.Comment;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.Board;
import lombok.Builder;
import lombok.Data;

@Data
public class CommentRequestDto {

    private String commentContents;

    private Board board;

    @Builder
    public CommentRequestDto(String commentContents){

        this.commentContents = commentContents;

    }
}
