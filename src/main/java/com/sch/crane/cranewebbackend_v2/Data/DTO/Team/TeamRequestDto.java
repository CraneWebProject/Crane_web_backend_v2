package com.sch.crane.cranewebbackend_v2.Data.DTO.Team;

import com.sch.crane.cranewebbackend_v2.Domain.Enums.TeamType;
import lombok.Builder;
import lombok.Data;

@Data
public class TeamRequestDto {

    private TeamType teamType;

    private String teamName;

    @Builder
    public TeamRequestDto(TeamType teamType, String teamName){
        this.teamType = teamType;
        this.teamName = teamName;
    }
}
