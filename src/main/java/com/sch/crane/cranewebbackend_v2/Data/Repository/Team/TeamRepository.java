package com.sch.crane.cranewebbackend_v2.Data.Repository.Team;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.Team;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.TeamType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    //teamType을 제외한 모든 팀을 검색
    List<Team> findByTeamTypeNot(TeamType teamType);

}
