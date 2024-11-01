package com.sch.crane.cranewebbackend_v2.Data.DTO.Team;

import com.sch.crane.cranewebbackend_v2.Domain.Enums.TeamType;
import lombok.Builder;
import lombok.Data;

@Data
public class TeamRequestDto {
    private Long tid;
    private TeamType teamType;

    private String teamName;

    @Builder
    public TeamRequestDto(Long tid, TeamType teamType, String teamName){
        this.tid = tid;
        this.teamType = teamType;
        this.teamName = teamName;
    }
}
