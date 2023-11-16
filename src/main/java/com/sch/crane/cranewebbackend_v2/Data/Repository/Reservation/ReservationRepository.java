package com.sch.crane.cranewebbackend_v2.Data.Repository.Reservation;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r where r.user.userEmail =: userEmail")
    List<Reservation> findByUserEmail(@Param("userEmail") String userEmail);


    List<Reservation> findByResStartTimeBetween(LocalDateTime resStartFrom, LocalDateTime resStartTo );
}
