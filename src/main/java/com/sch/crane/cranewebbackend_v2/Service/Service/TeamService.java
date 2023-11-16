package com.sch.crane.cranewebbackend_v2.Service.Service;

import com.sch.crane.cranewebbackend_v2.Data.DTO.Team.TeamMatchRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Team.TeamRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Team.TeamResponseDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Team.TeamMatchResponseDto;
import com.sch.crane.cranewebbackend_v2.Data.Repository.Team.TeamMatchRepository;
import com.sch.crane.cranewebbackend_v2.Data.Repository.Team.TeamRepository;
import com.sch.crane.cranewebbackend_v2.Data.Repository.User.UserRepository;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Team;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.TeamMatch;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.TeamType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMatchRepository teamMatchRepository;
    private final UserRepository userRepository;

    @Transactional
    public Team createTeam(TeamRequestDto teamRequestDto) {
        Team team = Team.builder()
                .teamType(teamRequestDto.getTeamType())
                .teamName(teamRequestDto.getTeamName())
                .build();
        return teamRepository.save(team);
    } // 팀 생성

    @Transactional
    public List<TeamResponseDto> readAllTeam() {
        List<Team> teamList = teamRepository.findAllDesc();
        List<TeamResponseDto> teamResponseDtoList = new ArrayList<>();
        for(Team t : teamList) {
            TeamResponseDto teamResponseDto = TeamResponseDto.builder()
                    .teamName(t.getTeamName())
                    .teamType(t.getTeamType())
                    .build();
            teamResponseDtoList.add(teamResponseDto);
        }
        return teamResponseDtoList;
    } // 모든 팀 조회
    @Transactional
    public TeamResponseDto readTeamById(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                ()-> new NoSuchElementException("팀이 존재하지 않습니다.")
        );
        TeamResponseDto teamResponseDto = TeamResponseDto.builder()
                .teamName(team.getTeamName())
                .teamType(team.getTeamType())
                .build();
        return teamResponseDto;
    } // 팀 정보 보기

    @Transactional
    public TeamResponseDto readTeamByName(String teamName) {
        Team team = teamRepository.findByTeamName(teamName);
        if(team==null)
        {
            throw new NoSuchElementException("팀이 존재하지 않습니다.");
        }
        TeamResponseDto teamResponseDto = TeamResponseDto.builder()
                .teamType(team.getTeamType())
                .teamName(team.getTeamName())
                .build();
        return teamResponseDto;
    } // 팀 명으로 팀 찾기

    @Transactional
    public List<TeamMatchResponseDto> readTeamMatchByTeam(Long teamId){
        Team team = teamRepository.findById(teamId).orElseThrow(
                ()-> new NoSuchElementException("팀이 존재하지 않습니다.")
        );
        List<TeamMatch> teamMatchList = teamMatchRepository.findTeamMatchByTeam(team);
        List<TeamMatchResponseDto> teamMatchResponseDtoList = new ArrayList<>();
        for(TeamMatch tm : teamMatchList){
            TeamMatchResponseDto teamMatchResponseDto = TeamMatchResponseDto.builder()
                    .teamMatchRole(tm.getTeamMatchRole())
                    .user(tm.getUser())
                    .build();
            teamMatchResponseDtoList.add(teamMatchResponseDto);
        }
        return teamMatchResponseDtoList;
    } // 팀으로 팀 매치 조회

    @Transactional
    public List<TeamResponseDto> readTeamByTeamType(TeamType teamType){
        List<Team> teamList = teamRepository.findTeamByteamType(teamType);
        List<TeamResponseDto> teamResponseDtoList = new ArrayList<>();
        for(Team t : teamList){
            TeamResponseDto teamResponseDto = TeamResponseDto.builder()
                    .teamName(t.getTeamName())
                    .teamType(t.getTeamType())
                    .build();
            teamResponseDtoList.add(teamResponseDto);
        }
        return teamResponseDtoList;
    } // 팀 타입으로 팀 조회

    @Transactional
    public Team editTeam(Long teamId, TeamRequestDto teamRequestDto) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                ()-> new NoSuchElementException("팀이 존재하지 않습니다.")
        );
        team.updateTeam(teamRequestDto.getTeamType(), teamRequestDto.getTeamName());
        return teamRepository.save(team);
    } // 팀 정보 변경

    @Transactional
    public List<TeamResponseDto> readTeamByUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new NoSuchElementException("유저가 존재하지 않습니다.")
        );
        List<TeamMatch> teamMatchList = teamMatchRepository.findTeamMatchByUser(user);
        List<TeamResponseDto> teamResponseDtoList = new ArrayList<>();
        for(TeamMatch tm : teamMatchList){
            Team team = tm.getTeam();
            TeamResponseDto teamResponseDto = TeamResponseDto.builder()
                    .teamType(team.getTeamType())
                    .teamName(team.getTeamName())
                    .build();
            teamResponseDtoList.add(teamResponseDto);
        }
        return teamResponseDtoList;
    } // 유저가 소속된 팀 조회

    @Transactional
    public void delTeam(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                ()-> new NoSuchElementException("팀이 존재하지 않습니다.")
        );
        List<TeamMatch> teamMatchList = teamMatchRepository.findTeamMatchByTeam(team);
        for(TeamMatch t: teamMatchList){
            teamMatchRepository.deleteById(t.getId());
            //delete(t)와 뭐가 더 성능이 좋을지
        }
        teamRepository.delete(team);
    } // 팀 매치 삭제 -> 팀 삭제

    @Transactional
    public void delTeamMatch(Long teamMatchId){
        TeamMatch teamMatch = teamMatchRepository.findById(teamMatchId).orElseThrow(
                ()-> new NoSuchElementException("매칭이 존재하지 않습니다.")
        );
        teamMatchRepository.delete(teamMatch);
    } // 팀 매치 삭제

    @Transactional
    public TeamMatch matchTeam(TeamMatchRequestDto teamMatchRequestDto){
        Team team = teamRepository.findById(teamMatchRequestDto.getTid()).orElseThrow(
                ()-> new NoSuchElementException("팀이 존재하지 않습니다.")
        );
        User user = userRepository.findById(teamMatchRequestDto.getUid()).orElseThrow(
                ()-> new NoSuchElementException("유저가 존재하지 않습니다.")
        );
        TeamMatch teamMatch = TeamMatch.builder()
                .team(team)
                .user(user)
                .teamMatchRole(teamMatchRequestDto.getTeamMatchRole())
                .build();
        return teamMatchRepository.save(teamMatch);
    } // 팀 매치 작성


}
