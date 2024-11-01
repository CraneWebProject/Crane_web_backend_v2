package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller;

import com.sch.crane.cranewebbackend_v2.Data.DTO.Team.TeamMatchRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Team.TeamRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Team.TeamResponseDto;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.Security.SecurityUtil;
import com.sch.crane.cranewebbackend_v2.Service.Service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;

    private final SecurityUtil securityUtil;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @PostMapping("/")
    public ResponseEntity<TeamResponseDto> createTeam(@RequestBody TeamRequestDto dto){
        return ResponseEntity.ok(teamService.createTeam(dto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @PutMapping("/{tid}")
    public ResponseEntity<TeamResponseDto> updateTeam(@PathVariable("tid") Long tid,
                                                      @RequestBody TeamRequestDto dto){
        String userEmail = securityUtil.getCurrentLoggedInUserEmail();
        dto.setTid(tid);
        return ResponseEntity.ok(teamService.updateTeam(userEmail, dto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @DeleteMapping("/{tid}")
    public ResponseEntity<?> deleteTeam(@PathVariable("tid") Long tid){
        String userEmail = securityUtil.getCurrentLoggedInUserEmail();
        teamService.deleteTeam(userEmail, tid);
        return ResponseEntity.ok(tid);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @GetMapping("/{tid}")
    public ResponseEntity<TeamResponseDto> getTeamListByTid(@PathVariable("tid") Long tid){
        return ResponseEntity.ok(teamService.findTeamByTid(tid));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @GetMapping("/list")
    public ResponseEntity<List<TeamResponseDto>> getTeamList(){
        return ResponseEntity.ok(teamService.findTeamList());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @PostMapping("/match")
    public ResponseEntity<TeamResponseDto> createTeamMatch(@RequestBody TeamMatchRequestDto dto){
        String userEmail = securityUtil.getCurrentLoggedInUserEmail();
        return ResponseEntity.ok(teamService.createTeamMatch(userEmail, dto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @PutMapping("/match/{tmid}")
    public ResponseEntity<TeamResponseDto> updateTeamMatch( @PathVariable("tmid") Long tmid,
                                                            @RequestBody TeamMatchRequestDto dto){
        String userEmail = securityUtil.getCurrentLoggedInUserEmail();
        dto.setTmid(tmid);
        return ResponseEntity.ok(teamService.updateTeamMatch(userEmail, dto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @DeleteMapping("/match/{tmid}")
    public ResponseEntity<TeamResponseDto> deleteTeamMatch( @PathVariable("tmid") Long tmid){
        String userEmail = securityUtil.getCurrentLoggedInUserEmail();
        return ResponseEntity.ok(teamService.deleteTeamMatch(userEmail, tmid));
    }

}
