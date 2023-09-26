package com.sch.crane.cranewebbackend_v2.Domain.Entity;

import com.sch.crane.cranewebbackend_v2.Domain.Enums.BoardCategory;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.BoardState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bid;

    private String boardTitle;

    private String boardContents;

    private Integer boardView;

    private BoardCategory boardCategory;

    private BoardState boardState;

    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    private Board(Long bid,Integer boardView, String boardTitle, String boardContents,
                  BoardCategory boardCategory,BoardState boardState, User user){

        this.bid = bid;

        this.boardView = boardView;

        this.boardTitle = boardTitle;

        this.boardContents = boardContents;

        this.boardCategory = boardCategory;

        this.boardState = boardState;

        this.user = user;
    }


}
