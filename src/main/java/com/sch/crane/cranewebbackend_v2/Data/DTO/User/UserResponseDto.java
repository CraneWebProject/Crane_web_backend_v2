package com.sch.crane.cranewebbackend_v2.Data.DTO.User;

import lombok.Builder;
import lombok.Data;
import org.hibernate.Session;

@Data
public class UserResponseDto {
    private String userName;

    private Session session;

    private Long uid;

    @Builder
    public UserResponseDto(String userName, Session session, Long uid){
        this.userName = userName;
        this.session = session;
        this.uid = uid;
    }

}
