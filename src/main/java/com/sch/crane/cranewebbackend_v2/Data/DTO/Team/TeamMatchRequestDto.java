package com.sch.crane.cranewebbackend_v2.Data.DTO.Team;

import com.sch.crane.cranewebbackend_v2.Domain.Enums.TeamMatchRole;
import lombok.Builder;
import lombok.Data;

@Data
public class TeamMatchRequestDto {
    private Long tid;

    private TeamMatchRole teamMatchRole;

    private Long uid;

    @Builder
    public TeamMatchRequestDto(Long tid, Long uid, TeamMatchRole teamMatchRole){
        this.tid = tid;
        this.uid = uid;
        this.teamMatchRole = teamMatchRole;
    }
}
