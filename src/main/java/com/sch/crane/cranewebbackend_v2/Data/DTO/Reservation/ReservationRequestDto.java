package com.sch.crane.cranewebbackend_v2.Data.DTO.Reservation;

import com.sch.crane.cranewebbackend_v2.Domain.Enums.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRequestDto {

    private LocalDateTime resStartTime;

    private LocalDateTime resEndTime;

    private Long rid;

    private ReservationStatus reservationStatus;

    @Builder
    public ReservationRequestDto(LocalDateTime resStartTime, LocalDateTime resEndTime, Long rid, ReservationStatus reservationStatus){
        this.resStartTime = resStartTime;
        this.resEndTime = resEndTime;
        this.rid = rid;
        this.reservationStatus = reservationStatus;
    }
}
