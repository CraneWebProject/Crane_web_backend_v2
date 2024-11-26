package com.sch.crane.cranewebbackend_v2.Data.Repository.Reservation;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.Instrument;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r where r.user.userEmail = :userEmail")
    List<Reservation> findByUserEmail(@Param("userEmail") String userEmail);

    @Query("select r from Reservation r where r.user.uid = :uid")
    List<Reservation> findByUid(@Param("uid") Long uid);

    @Query("select r from Reservation r where r.instrument.iid = :iid")
    List<Reservation> findByInstrumentId(@Param("iid") Long iid );

    Reservation findByResStartTimeAndInstrument(LocalDateTime resStartTime, Instrument instrument);

    @Query("SELECT r FROM Reservation r WHERE r.resStartTime >= :startOfDay AND r.resStartTime < :endOfDay AND r.instrument.iid = :iid")
    List<Reservation> findAllByResStartDateAndInstrument(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            @Param("iid") Long iid);


    @Query("SELECT r FROM Reservation r WHERE r.resStartTime = :resStartTime")
    List<Reservation> findByResStartTime(LocalDateTime resStartTime);

    @Query("SELECT r FROM Reservation r WHERE r.resStartTime BETWEEN :startOfDay AND :endOfDay")
    List<Reservation> findAllByDate(@Param("startOfDay") LocalDateTime startOfDay,
                                    @Param("endOfDay") LocalDateTime endOfDay);

}
