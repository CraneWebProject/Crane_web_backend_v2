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

    private Long boardNum;

    private String boardTitle;

    private String boardContents;

    private LocalDateTime boardCreatedTime;

    private BoardCategory boardCategory;

    private BoardState boardState;

    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    private Board(Long bid, Long boardNum, String boardTitle, String boardContents,LocalDateTime boardCreatedTime,
                  BoardCategory boardCategory,BoardState boardState, User user){

        this.bid = bid;

        this.boardNum = boardNum;

        this.boardTitle = boardTitle;

        this.boardContents = boardContents;

        this.boardCreatedTime = boardCreatedTime;

        this.boardCategory = boardCategory;

        this.boardState = boardState;

        this.user = user;
    }


}
