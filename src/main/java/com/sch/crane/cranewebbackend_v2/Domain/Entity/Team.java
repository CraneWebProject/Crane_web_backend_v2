package com.sch.crane.cranewebbackend_v2.Domain.Entity;

import com.sch.crane.cranewebbackend_v2.Domain.Enums.TeamType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;

    private TeamType teamType;

    private String teamName;

    public void updateTeam(TeamType teamType, String teamName)
    {
        this.teamType = teamType;
        this.teamName = teamName;
    }
    @Builder
    public Team(Long tid, TeamType teamType, String teamName){

        this.tid = tid;

        this.teamType = teamType;

        this.teamName = teamName;
    }
}
