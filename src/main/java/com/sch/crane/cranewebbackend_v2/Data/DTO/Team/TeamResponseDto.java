package com.sch.crane.cranewebbackend_v2.Data.DTO.Team;

import com.sch.crane.cranewebbackend_v2.Data.DTO.User.UserResponseDto;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.TeamType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class TeamResponseDto {

    private TeamType teamType;

    private String teamName;

    List<UserResponseDto> userResponseDtoList;

    @Builder
    public TeamResponseDto(TeamType teamType, String teamName,
                           List<UserResponseDto> userResponseDtoList){
        this.teamType =teamType;
        this.teamName =teamName;
        this.userResponseDtoList = userResponseDtoList;

    }

}
