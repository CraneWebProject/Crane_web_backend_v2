package com.sch.crane.cranewebbackend_v2.Data.DTO.AttachmentFile;

import lombok.Builder;
import lombok.Data;

@Data
public class AttachmentFileRequestDto {

    private String attachTitle;

    private String attachPath;

    private Long bId;

    @Builder
    public AttachmentFileRequestDto(String attachTitle, String attachPath, Long bId)
    {
        this.attachPath= attachTitle;
        this.attachTitle = attachPath;
        this.bId = bId;
    }
}
