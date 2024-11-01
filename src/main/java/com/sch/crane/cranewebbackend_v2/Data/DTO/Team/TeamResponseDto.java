package com.sch.crane.cranewebbackend_v2.Data.DTO.Team;

import com.sch.crane.cranewebbackend_v2.Data.DTO.User.UserResponseDto;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Team;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.TeamType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class TeamResponseDto {

    private Long tid;
    private TeamType teamType;

    private String teamName;

    List<UserResponseDto> userResponseDtoList;

    @Builder
    public TeamResponseDto( Long tid,
                            TeamType teamType,
                            String teamName,
                            List<UserResponseDto> userResponseDtoList){
        this.tid = tid;
        this.teamType =teamType;
        this.teamName =teamName;
        this.userResponseDtoList = userResponseDtoList;

    }

    public TeamResponseDto(Team team, List<UserResponseDto> userResponseDtoList){
        this.tid = team.getTid();
        this.teamName = team.getTeamName();
        this.teamType = team.getTeamType();
        this.userResponseDtoList = userResponseDtoList;
    }

}
