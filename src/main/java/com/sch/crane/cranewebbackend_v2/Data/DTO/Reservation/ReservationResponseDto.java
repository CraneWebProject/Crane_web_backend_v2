package com.sch.crane.cranewebbackend_v2.Data.DTO.Reservation;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.Reservation;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponseDto {

    private Long rid;
    private String resName;
    private LocalDateTime resStartTime;

    private ReservationStatus reservationStatus;
    private Boolean resPossible;
    private Long iid;

    private Long uid;

    private Long tid;

    @Builder
    public ReservationResponseDto( Long rid,
                                   String resName,
                                   LocalDateTime resStartTime,
                                   ReservationStatus reservationStatus,
                                   Boolean resPossible,
                                   Long iid,
                                   Long uid,
                                   Long tid){
        this.rid = rid;
        this.resName = resName;
        this.resStartTime = resStartTime;
        this.reservationStatus = reservationStatus;
        this.resPossible = resPossible;
        this.iid = iid;
        this.uid = uid;
        this.tid = tid;
    }

    public ReservationResponseDto(Reservation reservation){
        this.rid = reservation.getRid();
        this.resName = reservation.getResName();
        this.resStartTime = reservation.getResStartTime();
        this.reservationStatus = reservation.getReservationStatus();
        this.resPossible = reservation.getResPossible();
        this.iid = reservation.getInstrument().getIid();
        if(reservation.getUser() != null){
            this.uid = reservation.getUser().getUid();
        }
        if(reservation.getTeam() != null){
            this.tid = reservation.getTeam().getTid();
        }
    }
}
