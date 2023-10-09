package com.sch.crane.cranewebbackend_v2.Data.DTO.User;

import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserSession;
import lombok.Builder;
import lombok.Data;
import org.hibernate.Session;

@Data
public class UserResponseDto {
    private String userName;
    private String userEmail;
    private UserSession session;
    private Long uid;

    @Builder
    public UserResponseDto(String userName, String userEmail, UserSession session, Long uid){
        this.userName = userName;
        this.userEmail = userEmail;
        this.session = session;
        this.uid = uid;
    }

}
