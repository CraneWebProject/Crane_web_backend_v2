package com.sch.crane.cranewebbackend_v2.Data.DTO.Instrument;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.Instrument;
import lombok.Builder;
import lombok.Data;

@Data
public class InstrumentDto {
    private Long Iid;

    private String instName;

    private String instBirth;

    private Boolean instUsable;

    @Builder
    public InstrumentDto(Long Iid, String instName, String instBirth, Boolean instUsable){
        this.Iid = Iid;
        this.instName = instName;
        this.instBirth = instBirth;
        this.instUsable = instUsable;
    }

    public InstrumentDto(Instrument instrument){
        this.Iid = instrument.getIid();
        this.instName = instrument.getInstName();
        this.instBirth = instrument.getInstBirth();
        this.instUsable = instrument.getInstUsable();
    }
}
