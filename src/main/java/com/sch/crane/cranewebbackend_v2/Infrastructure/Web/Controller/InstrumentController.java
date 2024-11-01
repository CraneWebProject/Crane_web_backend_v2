package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller;

import com.sch.crane.cranewebbackend_v2.Data.DTO.Instrument.InstrumentDto;
import com.sch.crane.cranewebbackend_v2.Service.Service.InstrumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/inst")
public class InstrumentController {

    private final InstrumentService instrumentService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/")
    public ResponseEntity<InstrumentDto> createInst(@RequestBody InstrumentDto instrumentDto){
        return ResponseEntity.ok(instrumentService.createInst(instrumentDto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @GetMapping("/{instId}")
    public ResponseEntity<InstrumentDto> getInstById(@PathVariable Long instId){
        return ResponseEntity.ok(instrumentService.getInst(instId));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @DeleteMapping("/{instId}")
    public ResponseEntity<?> deleteInst(@PathVariable("instId") Long instId){
        instrumentService.delInst(instId);
        return ResponseEntity.ok(instId);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping("/{instId}")
    public ResponseEntity<InstrumentDto> updateInst(@PathVariable("instId") Long instId, @RequestBody InstrumentDto instrumentDto ){
        return ResponseEntity.ok(instrumentService.editInst(instId, instrumentDto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @GetMapping("/list")
    public ResponseEntity<List<InstrumentDto>> getInstList(){
        return ResponseEntity.ok(instrumentService.getInstList());
    }



}
