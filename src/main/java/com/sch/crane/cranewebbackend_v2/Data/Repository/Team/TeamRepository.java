package com.sch.crane.cranewebbackend_v2.Data.Repository.Team;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.Team;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.TeamType;
import io.lettuce.core.dynamic.annotation.Param;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
   @Query("Select t from Team t ORDER BY t.tid DESC")
   List<Team> findAllDesc();

   @Query("select t from Team t where t.teamName =: teamName")
   Team findByTeamName(@Param("teamName")String teamName);

   @Query("select t from Team t where t.teamType =: teamType")
   List<Team> findTeamByteamType(@Param("teamType")TeamType teamType);
}
