package com.sch.crane.cranewebbackend_v2.Service.Service;

import com.sch.crane.cranewebbackend_v2.Data.DTO.Instrument.InstrumentDto;
import com.sch.crane.cranewebbackend_v2.Data.Repository.Instrument.InstrumentRepository;
import com.sch.crane.cranewebbackend_v2.Data.Repository.Reservation.ReservationRepository;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Instrument;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Reservation;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;

    private final ReservationRepository reservationRepository;

    @Transactional
    public InstrumentDto createInst(InstrumentDto instrumentDto){

        if(instrumentDto.getInstUsable() == null){
            instrumentDto.setInstUsable(false);
        }

        Instrument instrument = Instrument.builder()
                .instName(instrumentDto.getInstName())
                .instBirth(instrumentDto.getInstBirth())
                .instUsable(instrumentDto.getInstUsable())
                .build();
        instrumentRepository.save(instrument);

        InstrumentDto dto = new InstrumentDto(instrument);

        return dto;
    }

    @Transactional
    public InstrumentDto editInst(Long Iid, InstrumentDto instrumentDto){
        Instrument instrument = instrumentRepository.findById(Iid).orElseThrow(
                () -> new NoSuchElementException("존재하지 않는 장비")
        );

        if(instrumentDto.getInstName() == null){
            instrumentDto.setInstName(instrument.getInstName());
        }

        if(instrumentDto.getInstBirth() == null){
            instrumentDto.setInstBirth(instrument.getInstBirth());
        }

        if(instrumentDto.getInstUsable() == null){
            instrumentDto.setInstUsable(instrument.getInstUsable());
        }

        instrument.updateInst(instrumentDto.getInstName(), instrumentDto.getInstBirth(), instrumentDto.getInstUsable());
        instrumentRepository.save(instrument);

        return new InstrumentDto(instrument);
    }


    @Transactional
    public void delInst(Long Iid){
        Instrument instrument = instrumentRepository.findById(Iid).orElseThrow(
                () -> new NoSuchElementException("존재하지 않는 장비")
        );

        List<Reservation> reservationList = reservationRepository.findByInstrumentId(Iid);
        for(Reservation r : reservationList){
            r.deleteInst();
        }

        reservationList = reservationRepository.findByInstrumentId(Iid);
        if(!reservationList.isEmpty()){
            throw new EntityExistsException("존재하는 예약이 있습니다.");
        }

        instrumentRepository.delete(instrument);
    }


    @Transactional
    public InstrumentDto getInst(Long Iid){
        Instrument instrument = instrumentRepository.findById(Iid).orElseThrow(
                () -> new NoSuchElementException("존재하지 않는 장비")
        );

        return new InstrumentDto(instrument);
    }

    @Transactional
    public List<InstrumentDto> getInstList(){
        List<Instrument> instrumentList = instrumentRepository.findAll();
        List<InstrumentDto> instrumentDtoList = new ArrayList<>();


        for(Instrument i : instrumentList){
            InstrumentDto dto = new InstrumentDto(i);
            instrumentDtoList.add(dto);
        }

        return instrumentDtoList;
    }

}
