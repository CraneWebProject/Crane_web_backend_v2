package com.sch.crane.cranewebbackend_v2.Domain.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;

    private String commentContents;

    @JoinColumn(name = "boardId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Builder
    public Comment(Long cid, String commentContents, Board board){
        this.cid = cid;

        this.commentContents = commentContents;

        this.board = board;
    }
}
