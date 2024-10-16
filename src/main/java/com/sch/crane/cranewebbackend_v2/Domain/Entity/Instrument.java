package com.sch.crane.cranewebbackend_v2.Domain.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Instrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Iid;

    @Column(nullable = false)
    private String instName;

    @Column(nullable = false)
    private Boolean instUsable;



    public void updateInst(Boolean instUsable){
        this.instUsable = instUsable;
    }

    @Builder
    public Instrument (String instName, Boolean instUsable){
        this.instName = instName;
        this.instUsable = instUsable;
    }

}
