package com.sch.crane.cranewebbackend_v2.Data.Repository.Team;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.TeamMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamMatchRepository extends JpaRepository<TeamMatch, Long> {

    @Query("select t from TeamMatch t where t.team.tid = :tid")
    List<TeamMatch> findByTid(@Param("tid") Long tid);

}
