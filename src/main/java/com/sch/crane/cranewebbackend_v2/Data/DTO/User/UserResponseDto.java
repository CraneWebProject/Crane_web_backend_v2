package com.sch.crane.cranewebbackend_v2.Data.DTO.User;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
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
    private String profilePic;
    private Integer userTh;

    @Builder
    public UserResponseDto(String userName, String userEmail, UserSession session, Long uid, String profilePic, Integer userTh){
        this.userName = userName;
        this.userEmail = userEmail;
        this.session = session;
        this.uid = uid;
        this.profilePic = profilePic;
        this.userTh = userTh;
    }

    public UserResponseDto(User user){
        this.userName = user.getUserName();
        this.userEmail = user.getUserEmail();
        this.session = user.getUserSession();
        this.uid = user.getUid();
        this.profilePic = user.getUserProfilePic();
        this.userTh = user.getUserTh();
    }

}
