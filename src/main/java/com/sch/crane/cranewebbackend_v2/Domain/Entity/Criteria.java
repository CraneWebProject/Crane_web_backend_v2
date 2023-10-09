package com.sch.crane.cranewebbackend_v2.Domain.Entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.service.annotation.GetExchange;

@Getter
public class Criteria {
    private int pageNum; // 현재 페이지
    private int pageAmount; // 페이지당 보여줄 게시글 수

    public Criteria() {
        this.pageNum = 1;
        this.pageAmount = 10;
    } // 목록을 처음 클릭했을 때 1페이지, 게시글 10개 출력

    public void setPageNum(int pageNum) {
        if(pageNum<=0) { this.pageNum =1;}
        else { this.pageNum = pageNum; }
    }
    public void setPageAmount(int pageAmount) { this.pageAmount = pageAmount; }
}
