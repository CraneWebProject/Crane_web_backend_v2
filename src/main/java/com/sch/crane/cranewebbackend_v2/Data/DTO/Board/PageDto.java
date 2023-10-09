package com.sch.crane.cranewebbackend_v2.Data.DTO.Board;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.Criteria;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDto {
    private int startPage; // 시작 페이지
    private int endPage; // 끝 페이지
    private int totalPage; // 전체 페이지
    private int totalBoard; // 전체 게시글 수
    private Criteria criteria; //

    public PageDto(Criteria criteria, int totalBoard) {
        this.criteria = criteria;
        this.totalBoard = totalBoard;

        this.endPage = (int)(Math.ceil(criteria.getPageNum()/10.0)*10);
        this.startPage = this.endPage-9;
        this.totalPage = (int)(Math.ceil(totalBoard/(double)criteria.getPageAmount()));
    }

}
