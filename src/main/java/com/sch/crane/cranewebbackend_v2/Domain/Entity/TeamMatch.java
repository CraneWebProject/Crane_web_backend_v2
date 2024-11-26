package com.sch.crane.cranewebbackend_v2.Domain.Entity;

import com.sch.crane.cranewebbackend_v2.Domain.Enums.TeamMatchRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMatch implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TeamMatchRole teamMatchRole;

    @JoinColumn(name = "uid")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @Builder
    public TeamMatch(TeamMatchRole teamMatchRole, User user, Team team){
        this.teamMatchRole = teamMatchRole;

        this.user = user;

        this.team = team;
    }

    public void updateTeamMatch(TeamMatchRole teamMatchRole){
        this.teamMatchRole = teamMatchRole;
    }
}
