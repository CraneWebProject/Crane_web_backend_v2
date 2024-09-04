package com.sch.crane.cranewebbackend_v2.Data.DTO.Board;

import com.sch.crane.cranewebbackend_v2.Domain.Enums.BoardCategory;
import lombok.Builder;
import lombok.Data;

@Data
public class BoardPageDto {
    private int page;
    private int size;
    private String sort;
    private BoardCategory boardCategory;

    @Builder
    public BoardPageDto(int page, int size, String sort, BoardCategory boardCategory){
        this.page = page;
        this.size = size;
        this.sort = sort;
        this.boardCategory = boardCategory;
    }
}
