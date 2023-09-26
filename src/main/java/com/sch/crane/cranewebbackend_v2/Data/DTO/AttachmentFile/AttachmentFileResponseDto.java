package com.sch.crane.cranewebbackend_v2.Data.DTO.AttachmentFile;

import lombok.Builder;
import lombok.Data;

@Data
public class AttachmentFileResponseDto {
    private String attachTitle;

    private String attachPath;

    @Builder
    public AttachmentFileResponseDto(String attachTitle, String attachPath){
        this.attachTitle = attachTitle;
        this.attachPath = attachPath;
    }
}
