package com.sch.crane.cranewebbackend_v2.Data.Repository.Team;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.Team;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.TeamMatch;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamMatchRepository extends JpaRepository<TeamMatch, Long> {
    @Query("select tm From TeamMatch tm where Team =: team")
    List<TeamMatch> findTeamMatchByTeam(@Param("team") Team team);

    @Query("select tm From TeamMatch tm where User =: user")
    List<TeamMatch> findTeamMatchByUser(@Param("user") User user);
}
