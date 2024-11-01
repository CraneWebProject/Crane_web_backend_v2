package com.sch.crane.cranewebbackend_v2.Domain.Entity;

import com.sch.crane.cranewebbackend_v2.Domain.Enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;

    private String resName;

    @Column(nullable = false)
    private LocalDateTime resStartTime;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @Column(nullable = false)
    private Boolean resPossible;

    @JoinColumn(name = "iid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Instrument instrument;

    @JoinColumn(name = "uid")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "tid")
    @ManyToOne(fetch =FetchType.LAZY)
    private Team team;


    public void updateReservation( String resName,
                                   Boolean resPossible,
                                   ReservationStatus reservationStatus,
                                   User user,
                                   Team team){
        this.resName = resName;
        this.resPossible = resPossible;
        this.reservationStatus = reservationStatus;
        this.user = user;
        this.team = team;
    }

    public void cancelReservation(){
        this.resName = null;
        this.reservationStatus = null;
        this.resPossible = true;
        this.instrument = null;
        this.user = null;
        this.team = null;
    }

    public void deleteInst(){
        this.instrument = null;
    }

    @Builder
    public Reservation( String resName,
                        LocalDateTime resStartTime,
                        ReservationStatus reservationStatus,
                        Boolean resPossible,
                        User user,
                        Team team,
                        Instrument instrument){
        this.resName = resName;
        this.resStartTime = resStartTime;
        this.reservationStatus = reservationStatus;
        this.resPossible = resPossible;
        this.user = user;
        this.team = team;
        this.instrument = instrument;
    }
}
