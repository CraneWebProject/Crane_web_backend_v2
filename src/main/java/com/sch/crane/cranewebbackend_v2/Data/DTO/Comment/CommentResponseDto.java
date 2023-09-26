package com.sch.crane.cranewebbackend_v2.Data.DTO.Comment;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.Board;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import lombok.Builder;
import lombok.Data;

@Data
public class CommentResponseDto {

    private Long cid;

    private String commentContents;

    private String userName;

    private Long uid;

    private Board board;

    @Builder
    public CommentResponseDto(Long cid, String commentContents, Board board,
                              String userName, Long uid){
        this.cid = cid;

        this.commentContents = commentContents;

        this.board = board;

        this.userName = userName;

        this.uid = uid;
    }
}
