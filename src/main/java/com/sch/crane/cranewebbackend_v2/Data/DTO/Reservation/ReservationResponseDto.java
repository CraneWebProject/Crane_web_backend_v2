package com.sch.crane.cranewebbackend_v2.Data.DTO.Reservation;

import com.sch.crane.cranewebbackend_v2.Domain.Enums.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponseDto {

    private LocalDateTime resStartTime;

    private LocalDateTime resEndTime;

    private ReservationStatus reservationStatus;

    private Long uid;

    private String userName;

    @Builder
    public ReservationResponseDto(LocalDateTime resStartTime, LocalDateTime resEndTime,
                                  ReservationStatus reservationStatus, Long uid, String userName){
        this.resStartTime = resStartTime;
        this.resEndTime = resEndTime;
        this.reservationStatus = reservationStatus;
        this.uid = uid;
        this.userName = userName;
    }
}
