package com.sch.crane.cranewebbackend_v2.Data.Repository.Board;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.Board;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.BoardCategory;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("select b from Board b where b.boardCategory =: boardCategory")
    List<Board> findBoardByCategory(@Param("boardCategory") BoardCategory boardCategory);

    @Query("select b from Board b where b.user =: uesr")
    List<Board> findBoardByUser(@Param("user") User user);

    @Query("update Board b set b.boardView = b.boardView +1 where b.bid =: bId")
    int increaseView(Long bId);
}
