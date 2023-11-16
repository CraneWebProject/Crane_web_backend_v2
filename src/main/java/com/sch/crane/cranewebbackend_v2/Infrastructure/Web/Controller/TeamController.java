package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller;


import com.sch.crane.cranewebbackend_v2.Data.DTO.Team.TeamMatchRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Team.TeamMatchResponseDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Team.TeamRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Team.TeamResponseDto;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Team;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.TeamMatch;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.TeamType;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.StatusCode;
import com.sch.crane.cranewebbackend_v2.Service.Service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {
    private final TeamService teamService;

    @GetMapping("/")
    public ResponseEntity<List<TeamResponseDto>> readAllTeam(){
        List<TeamResponseDto> teamResponseDtoList = teamService.readAllTeam();
        return ResponseEntity.status(StatusCode.OK).body(teamResponseDtoList);
    }
    @PostMapping("/createTeam")
    public ResponseEntity<Team> createTeam(@RequestBody TeamRequestDto teamRequestDto){
        Team team = teamService.createTeam(teamRequestDto);
        return ResponseEntity.status(StatusCode.CREATED).body(team);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponseDto> getTeamById(@PathVariable Long teamId){
       TeamResponseDto teamResponseDto = teamService.readTeamById(teamId);
       return ResponseEntity.status(StatusCode.OK).body(teamResponseDto);
    }

    @PutMapping("/updateTeam/{teamId}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long teamId, @RequestBody TeamRequestDto teamRequestDto) {
        Team team = teamService.editTeam(teamId, teamRequestDto);
        return ResponseEntity.status(StatusCode.OK).body(team);
    }

    @DeleteMapping("/delTeam/{teamId}")
    public ResponseEntity<String> delTeam(@PathVariable Long teamId){
        teamService.delTeam(teamId);
        return ResponseEntity.status(StatusCode.OK).body("팀 삭제됨");
    }

    @GetMapping("/{teamName}")
    public ResponseEntity<TeamResponseDto> readTeamByname(@PathVariable String teamName){
        TeamResponseDto teamResponseDto = teamService.readTeamByName(teamName);
        return ResponseEntity.status(StatusCode.OK).body(teamResponseDto);
    }

    @GetMapping("/teamMatch/{teamId}")
    public ResponseEntity<List<TeamMatchResponseDto>> readTeamMatchByTeam(@PathVariable Long teamId){
        List<TeamMatchResponseDto> teamMatchResponseDtoList = teamService.readTeamMatchByTeam(teamId);
        return ResponseEntity.status(StatusCode.OK).body(teamMatchResponseDtoList);
    }

    @GetMapping("/teamMatch/{teamType}")
    public ResponseEntity<List<TeamResponseDto>> readTeamByTeamType(@PathVariable TeamType teamType){
        List<TeamResponseDto> teamResponseDtoList = teamService.readTeamByTeamType(teamType);
        return ResponseEntity.status(StatusCode.OK).body(teamResponseDtoList);
    }

    @GetMapping("/teamMatch/{User}")
    public ResponseEntity<List<TeamResponseDto>> readTeamByUser(@PathVariable Long userId){
        List<TeamResponseDto>  teamResponseDtoList = teamService.readTeamByUser(userId);
        return ResponseEntity.status(StatusCode.OK).body(teamResponseDtoList);
    }

    @DeleteMapping("/teamMatch/delMatch/{teamMatchId}")
    public ResponseEntity<String> delTeamMatch(@PathVariable Long teamMatchId){
        teamService.delTeamMatch(teamMatchId);
        return ResponseEntity.status(StatusCode.OK).body("팀 매치 삭제됨");
    }

    @PostMapping("/createTeamMatch")
    public ResponseEntity<TeamMatch> createTeamMatch(@RequestBody TeamMatchRequestDto teamMatchRequestDto){
        TeamMatch teamMatch = teamService.matchTeam(teamMatchRequestDto);
        return ResponseEntity.status(StatusCode.CREATED).body(teamMatch);
    }
}

