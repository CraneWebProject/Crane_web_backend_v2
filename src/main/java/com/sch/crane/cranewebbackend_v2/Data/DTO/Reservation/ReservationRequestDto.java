package com.sch.crane.cranewebbackend_v2.Data.DTO.Reservation;

import com.sch.crane.cranewebbackend_v2.Data.DTO.Instrument.InstrumentDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Team.TeamResponseDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.User.UserResponseDto;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRequestDto {

    private Long rid;
    private String resName;
    private LocalDateTime resStartTime;


    private UserResponseDto userResponseDto;

    private TeamResponseDto teamResponseDto;

    private InstrumentDto instrumentDto;

    private ReservationStatus reservationStatus;

    @Builder
    public ReservationRequestDto( Long rid,
                                  String resName,
                                  LocalDateTime resStartTime,
                                  UserResponseDto userResponseDto,
                                  TeamResponseDto teamResponseDto,
                                  InstrumentDto instrumentDto,
                                  ReservationStatus reservationStatus){
        this.rid = rid;
        this.resName = resName;
        this.resStartTime = resStartTime;
        this.userResponseDto = userResponseDto;
        this.teamResponseDto = teamResponseDto;
        this.instrumentDto = instrumentDto;
        this.reservationStatus = reservationStatus;
    }
}
