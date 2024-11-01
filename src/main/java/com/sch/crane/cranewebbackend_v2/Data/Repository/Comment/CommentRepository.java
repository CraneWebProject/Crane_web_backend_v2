package com.sch.crane.cranewebbackend_v2.Data.Repository.Comment;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.board.bid = :bId")
    List<Comment> findByBId(@Param("bId")Long bId);
}
