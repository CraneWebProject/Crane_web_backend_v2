package com.sch.crane.cranewebbackend_v2.Service.Service;

import com.sch.crane.cranewebbackend_v2.Data.DTO.Team.TeamMatchRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Team.TeamRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Team.TeamResponseDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.User.UserResponseDto;
import com.sch.crane.cranewebbackend_v2.Data.Repository.Team.TeamMatchRepository;
import com.sch.crane.cranewebbackend_v2.Data.Repository.Team.TeamRepository;
import com.sch.crane.cranewebbackend_v2.Data.Repository.User.UserRepository;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Team;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.TeamMatch;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.TeamMatchRole;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.TeamType;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserRole;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMatchRepository teamMatchRepository;

    //팀 생성
    @Transactional
    public TeamResponseDto createTeam(TeamRequestDto dto){
        Team team = Team.builder()
                .teamName(dto.getTeamName())
                .teamType(dto.getTeamType())
                .build();

        Long tid =teamRepository.save(team).getTid();

        TeamResponseDto teamResponseDto = TeamResponseDto.builder()
                .tid(tid)
                .teamName(team.getTeamName())
                .teamType(team.getTeamType())
                .build();

        return teamResponseDto;
    }

    //팀 수정
    //관리자, 어드민, 팀장만 수정 가능
    @Transactional
    public TeamResponseDto updateTeam(String userEmail, TeamRequestDto dto){
        Team team = teamRepository.findById(dto.getTid()).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 팀")
        );
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(EntityNotFoundException::new  );

        if (!user.getUid().equals(getTeamLead(team.getTid()).getUid()) &&
                !(user.getUserRole() == UserRole.ROLE_ADMIN || user.getUserRole() == UserRole.ROLE_MANAGER)) {
            throw new BadCredentialsException("권한이 없는 사용자");
        }


        if(dto.getTeamName() == null){
            dto.setTeamName(team.getTeamName());
        }
        if(dto.getTeamType() == null){
            dto.setTeamType(team.getTeamType());
        }

        team.updateTeam(dto.getTeamType(), dto.getTeamName());
        teamRepository.save(team);

        TeamResponseDto teamResponseDto = TeamResponseDto.builder()
                .tid(dto.getTid())
                .teamName(team.getTeamName())
                .teamType(team.getTeamType())
                .build();

        return teamResponseDto;
    }

    //팀 삭제
    //관리자, 어드민, 팀장만 수정 가능
    @Transactional
    public void deleteTeam(String userEmail, Long tid){
        Team team = teamRepository.findById(tid).orElseThrow(EntityNotFoundException::new);
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(EntityNotFoundException::new  );

        if (!user.getUid().equals(getTeamLead(team.getTid()).getUid()) &&
                !(user.getUserRole() == UserRole.ROLE_ADMIN || user.getUserRole() == UserRole.ROLE_MANAGER)) {
            throw new BadCredentialsException("권한이 없는 사용자");
        }

        //teamMatch 삭제
        List<TeamMatch> teamMatchList = teamMatchRepository.findByTid(tid);
        for(TeamMatch t : teamMatchList){
            teamMatchRepository.delete(t);
        }

        //삭제 확인
        teamMatchList = teamMatchRepository.findByTid(tid);
        if(!teamMatchList.isEmpty()){
            throw new EntityExistsException("팀원이 삭제되지 않음");
        }

        teamRepository.delete(team);
    }

    //팀 조회
    //팀원 포함
    @Transactional
    public TeamResponseDto findTeamByTid(Long tid){
        Team team = teamRepository.findById(tid).orElseThrow(EntityNotFoundException::new);
        User user;
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();

        //팀원 조회
        List<TeamMatch> teamMatchList = teamMatchRepository.findByTid(tid);
        //팀원 Dto로 변환
        for(TeamMatch t : teamMatchList){
            user = userRepository.findById(t.getUser().getUid()).orElseThrow(EntityNotFoundException::new);
            UserResponseDto userResponseDto = new UserResponseDto(user);
            userResponseDtoList.add(userResponseDto);
        }

        return  new TeamResponseDto(team, userResponseDtoList);
    }

    //팀 목록 조회
    //팀원은 포함하지 않음
    @Transactional
    public List<TeamResponseDto> findTeamList(){
        //비활성화 된 팀을 빼고 조회
        List<Team> teamList = teamRepository.findByTeamTypeNot(TeamType.DEACTIVATED_TEAM);
        List<TeamResponseDto> teamResponseDtoList = new ArrayList<>();

        for(Team t : teamList){
            TeamResponseDto teamResponseDto = TeamResponseDto.builder()
                    .tid(t.getTid())
                    .teamName(t.getTeamName())
                    .teamType(t.getTeamType())
                    .build();
            teamResponseDtoList.add(teamResponseDto);
        }

        return teamResponseDtoList;
    }

    //팀원 매치 생성( 팀원 추가 )
    //관리자, 어드민, 팀장만 생성 가능
    @Transactional
    public TeamResponseDto createTeamMatch(String userEmail, TeamMatchRequestDto teamMatchRequestDto){
        Team team = teamRepository.findById(teamMatchRequestDto.getTid()).orElseThrow(EntityNotFoundException::new);
        User user = userRepository.findById(teamMatchRequestDto.getUid()).orElseThrow(EntityNotFoundException::new);
        User LoggedInUser = userRepository.findByUserEmail(userEmail).orElseThrow(EntityNotFoundException::new );

        if (!LoggedInUser.getUid().equals(getTeamLead(team.getTid()).getUid()) &&
                !(LoggedInUser.getUserRole() == UserRole.ROLE_ADMIN || LoggedInUser.getUserRole() == UserRole.ROLE_MANAGER)) {
            throw new BadCredentialsException("권한이 없는 사용자");
        }

        TeamMatch teamMatch = TeamMatch.builder()
                .team(team)
                .user(user)
                .teamMatchRole(teamMatchRequestDto.getTeamMatchRole())
                .build();

        teamMatchRepository.save(teamMatch);

        return findTeamByTid(team.getTid());
    }

    //팀 매치 수정
    @Transactional
    public TeamResponseDto updateTeamMatch(String userEmail, TeamMatchRequestDto teamMatchRequestDto){
        TeamMatch teamMatch = teamMatchRepository.findById(teamMatchRequestDto.getTmid()).orElseThrow(EntityNotFoundException::new);
        User LoggedInUser = userRepository.findByUserEmail(userEmail).orElseThrow(EntityNotFoundException::new  );
        if (!LoggedInUser.getUid().equals(getTeamLead(teamMatch.getTeam().getTid()).getUid()) &&
                !(LoggedInUser.getUserRole() == UserRole.ROLE_ADMIN || LoggedInUser.getUserRole() == UserRole.ROLE_MANAGER)) {
            throw new BadCredentialsException("권한이 없는 사용자");
        }


        teamMatch.updateTeamMatch(teamMatchRequestDto.getTeamMatchRole());
        teamMatchRepository.save(teamMatch);

        return findTeamByTid(teamMatchRequestDto.getTid());
    }

    //팀 매치 삭제
    @Transactional
    public TeamResponseDto deleteTeamMatch(String userEmail, Long tmid){
        User LoggedInUser = userRepository.findByUserEmail(userEmail).orElseThrow(EntityNotFoundException::new  );
        TeamMatch teamMatch = teamMatchRepository.findById(tmid).orElseThrow();
        Long tid = teamMatch.getTeam().getTid();

        if (!LoggedInUser.getUid().equals(getTeamLead(teamMatch.getTeam().getTid()).getUid()) &&
                !(LoggedInUser.getUserRole() == UserRole.ROLE_ADMIN || LoggedInUser.getUserRole() == UserRole.ROLE_MANAGER)) {
            throw new BadCredentialsException("권한이 없는 사용자");
        }

        //deleteById를 사용시 내부적으로 null check 이후 삭제 진행
        teamMatchRepository.deleteById(tmid);

        return findTeamByTid(tid);
    }

    //get team Lead
    @Transactional
    public User getTeamLead(Long tid){
        List<TeamMatch> teamMatchList = teamMatchRepository.findByTid(tid);

        for(TeamMatch tm : teamMatchList){
            if(tm.getTeamMatchRole() == TeamMatchRole.LEADER){
                return tm.getUser();
            }
        }

        throw new EntityNotFoundException();
    }

}
