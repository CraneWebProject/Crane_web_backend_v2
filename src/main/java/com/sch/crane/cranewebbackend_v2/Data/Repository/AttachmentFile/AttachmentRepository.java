package com.sch.crane.cranewebbackend_v2.Data.Repository.AttachmentFile;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.AttachmentFile;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Board;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<AttachmentFile, Long> {
    @Query("select a from AttachmentFile a where a.board =: board")
    List<AttachmentFile> findAttachmentFileByBoardId(@Param("board") Long bId);

}
