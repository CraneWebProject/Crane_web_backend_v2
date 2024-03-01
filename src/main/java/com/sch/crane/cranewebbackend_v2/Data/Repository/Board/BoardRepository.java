package com.sch.crane.cranewebbackend_v2.Data.Repository.Board;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.Board;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.BoardCategory;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Override
    @Query("select b from Board b where b.bid = :bid")
    Optional<Board> findById(@Param("bid") Long bid);

    @Query("select b from Board b where b.boardCategory =:boardCategory")
    List<Board> findBoardByCategory(@Param("boardCategory") BoardCategory boardCategory);

    @Query("select b from Board b where b.user =: user")
    List<Board> findBoardByUser(@Param("user") User user);

    @Modifying
    @Query("update Board b set b.boardView = b.boardView +1 where b.bid = :bid")
    int increaseView(@Param("bid") Long bid);
}
