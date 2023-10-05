package com.sch.crane.cranewebbackend_v2.Domain.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aid;

    private String attachTitle;

    private String attachPath;

    @JoinColumn(name = "boardId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    public void updateAttachmentFile(String attachTitle, String attachPath)
    {
        this.attachTitle = attachTitle;
        this.attachPath = attachPath;
    }

    @Builder
    private AttachmentFile(Long aid, String attachTitle, String attachPath, Board board){

        this.aid = aid;

        this.attachTitle = attachTitle;

        this.attachPath = attachPath;

        this.board = board;
    }

}
