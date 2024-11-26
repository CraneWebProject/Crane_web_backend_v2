package com.sch.crane.cranewebbackend_v2.Data.Repository.Instrument;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {

    @Query("select i from Instrument i where i.instUsable = :instUsable")
    List<Instrument> findByInstUsable(@Param("instUsable") Boolean instUsable);

}
