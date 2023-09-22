package com.sch.crane.cranewebbackend_v2.Domain.Entity;

import com.sch.crane.cranewebbackend_v2.Domain.Enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime resStartTime;

    private LocalDateTime resEndTime;

    private ReservationStatus reservationStatus;

    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Reservation(LocalDateTime resStartTime, LocalDateTime resEndTime, ReservationStatus reservationStatus, User user){
        this.resStartTime = resStartTime;

        this.resEndTime = resEndTime;

        this.reservationStatus = reservationStatus;

        this.user = user;
    }


}
