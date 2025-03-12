package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller;

import com.sch.crane.cranewebbackend_v2.Data.DTO.Reservation.ReservationRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Reservation.ReservationResponseDto;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.Security.SecurityUtil;
import com.sch.crane.cranewebbackend_v2.Service.Service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final SecurityUtil securityUtil;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/")
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto dto){
        return ResponseEntity.ok(reservationService.createReservation(dto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @GetMapping("/{resId}")
    public ResponseEntity<ReservationResponseDto> getReservation(@PathVariable("resId") Long resId){
        return ResponseEntity.ok(reservationService.getReservation(resId));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @PutMapping("/{resId}")
    public ResponseEntity<ReservationResponseDto> updateReservation(@PathVariable("resId") Long resId,
                                                                    @RequestBody ReservationRequestDto dto){
        String userEmail = securityUtil.getCurrentLoggedInUserEmail();
        dto.setRid(resId);
        return ResponseEntity.ok(reservationService.updateReservation(userEmail, dto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @GetMapping("/makeres/{resId}")
    public ResponseEntity<ReservationResponseDto> makeAReservation(@PathVariable("resId") Long resId){
        String userEmail = securityUtil.getCurrentLoggedInUserEmail();
        return ResponseEntity.ok(reservationService.makeAReservation( userEmail, resId));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @GetMapping("/{resId}/cancel")
    public ResponseEntity<ReservationResponseDto> cancelReservation(@PathVariable("resId") Long resId){
        String userEmail = securityUtil.getCurrentLoggedInUserEmail();
        return ResponseEntity.ok(reservationService.cancelReservation(userEmail, resId));
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @GetMapping("/date/{date}")
    public ResponseEntity<List<ReservationResponseDto>> getResByDate(@PathVariable("date")
                                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                     LocalDate date,
                                                                     @RequestParam (value = "iid") Long iid){
        return ResponseEntity.ok(reservationService.findResByDayAndInst(date, iid));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>("Invalid date format. Please use 'yyyy-MM-dd'.", HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @GetMapping("/user/{uid}")
    public ResponseEntity<List<ReservationResponseDto>> getResByUserId(@PathVariable("uid") Long uid){
        return ResponseEntity.ok(reservationService.findResByUser(uid));
    }


}
