package com.sch.crane.cranewebbackend_v2.Data.DTO.Board;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.Board;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class BoardPageListDto {

        private List<BoardResponseDto> contents;
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean last;
        private boolean first;
        private boolean empty;

        public BoardPageListDto(Page<Board> boardPage){
                this.contents = boardPage.getContent().stream()
                        .map(BoardResponseDto::new) // Board 엔티티를 BoardDto로 변환
                        .collect(Collectors.toList());
                this.pageNumber = boardPage.getNumber();
                this.pageSize = boardPage.getSize();
                this.totalElements = boardPage.getTotalElements();
                this.totalPages = boardPage.getTotalPages();
                this.last = boardPage.isLast();
                this.first = boardPage.isFirst();
                this.empty = boardPage.isEmpty();
        }

}
