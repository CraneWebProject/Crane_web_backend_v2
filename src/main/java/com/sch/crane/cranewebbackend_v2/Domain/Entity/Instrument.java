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
    private Long iid;

    @Column(nullable = false)
    private String instName;

    @Column(nullable = true)
    private String instBirth;

    @Column(nullable = false)
    private Boolean instUsable;



    public void updateInst(String instName, String instBirth, Boolean instUsable){
        this.instName = instName;
        this.instBirth = instBirth;
        this.instUsable = instUsable;
    }

    @Builder
    public Instrument (String instName, String instBirth ,Boolean instUsable){
        this.instName = instName;
        this.instBirth = instBirth;
        this.instUsable = instUsable;
    }

}
