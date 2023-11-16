package com.sch.crane.cranewebbackend_v2.Data.DTO.Team;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.TeamMatchRole;
import lombok.Builder;
import lombok.Data;

@Data
public class TeamMatchResponseDto {
    private TeamMatchRole teamMatchRole;
    private User user;

    @Builder
    public TeamMatchResponseDto(TeamMatchRole teamMatchRole, User user){
        this.teamMatchRole = teamMatchRole;
        this.user = user;
    }
}
