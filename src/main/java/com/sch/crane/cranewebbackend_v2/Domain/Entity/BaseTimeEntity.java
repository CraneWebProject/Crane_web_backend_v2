package com.sch.crane.cranewebbackend_v2.Domain.Entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
//    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime createdDate;

    @LastModifiedDate
//    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime modifiedDate;
}
