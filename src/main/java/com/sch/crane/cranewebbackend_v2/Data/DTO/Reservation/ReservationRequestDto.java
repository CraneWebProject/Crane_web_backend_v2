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


    private Long uid;

    private Long tid;

    private Long iid;

    private ReservationStatus reservationStatus;

    @Builder
    public ReservationRequestDto( Long rid,
                                  String resName,
                                  LocalDateTime resStartTime,
                                  Long uid,
                                  Long tid,
                                  Long iid,
                                  ReservationStatus reservationStatus){
        this.rid = rid;
        this.resName = resName;
        this.resStartTime = resStartTime;
        this.uid = uid;
        this.tid = tid;
        this.iid = iid;
        this.reservationStatus = reservationStatus;
    }
}
