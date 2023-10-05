package com.sch.crane.cranewebbackend_v2.Domain.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;

    private String commentContents;

    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "boardId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    public void updateComment(String commentContents)
    {
        this.commentContents = commentContents;
    }
    @Builder
    public Comment(Long cid, String commentContents, Board board, User user){
        this.cid = cid;

        this.commentContents = commentContents;

        this.board = board;

        this.user = user;
    }
}
