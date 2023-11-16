package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller;

import com.sch.crane.cranewebbackend_v2.Data.DTO.Reservation.ReservationRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Reservation.ReservationResponseDto;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Response.GeneralResponse;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.JWT.UserDetailsImpl;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.ResponseMessage;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.StatusCode;
import com.sch.crane.cranewebbackend_v2.Service.Service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;


    //예약 생성 요청 처리
    @PostMapping("/reservation")
    public ResponseEntity<?> CreateReservation(@RequestBody ReservationRequestDto dto){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        String userEmail = userDetails.getUserEmail();

        ReservationResponseDto reservationResponseDto =  reservationService.createReservation(userEmail, dto);

        GeneralResponse response = GeneralResponse.builder()
                .code(StatusCode.OK)
                .message(ResponseMessage.CREATE_OK)
                .data(reservationResponseDto)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/reservation/{ReservationId}")
    public ResponseEntity<?> getReservationByReservationId(@PathVariable Long ReservationId){
        ReservationResponseDto responseDto = reservationService.readReservation(ReservationId);

        GeneralResponse response = GeneralResponse.builder()
                .code(StatusCode.OK)
                .message(ResponseMessage.CHECK_OK)
                .data(responseDto)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/user")
    public ResponseEntity<?> readReservationByUserEmail(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        String userEmail = userDetails.getUserEmail();

        List<ReservationResponseDto> responseDto = reservationService.getReservationWithUserEmail(userEmail);

        GeneralResponse response = GeneralResponse.builder()
                .code(StatusCode.OK)
                .message(ResponseMessage.CHECK_OK)
                .data(responseDto)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/weekly")
    public ResponseEntity<?> readReservationListByWeek(LocalDate date){
        List<ReservationResponseDto> reservationResponseDtoList = reservationService.getReservationOfWeekWithDate(date);

        GeneralResponse response = GeneralResponse.builder()
                .code(StatusCode.OK)
                .message(ResponseMessage.CHECK_OK)
                .data(reservationResponseDtoList)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //예약정보 수정
    @PatchMapping("/updateReservation")
    public ResponseEntity<?> updateReservation(ReservationRequestDto dto){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        String userEmail = userDetails.getUserEmail();

        GeneralResponse response;

        try{
            ReservationResponseDto responseDto =  reservationService.updateReservation(userEmail, dto);

            response = GeneralResponse.builder()
                    .code(StatusCode.OK)
                    .message(ResponseMessage.UPDATE_OK)
                    .data(responseDto)
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);

        }catch (BadCredentialsException e){
            response = GeneralResponse.builder()
                    .code(StatusCode.BAD_REQUEST)
                    .message(ResponseMessage.UPDATE_FAILED)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long reservationId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        String userEmail = userDetails.getUserEmail();

        GeneralResponse response;


        try{
            boolean result = reservationService.deleteReservation(userEmail, reservationId);

            if(result){
                response = GeneralResponse.builder()
                        .code(StatusCode.OK)
                        .message(ResponseMessage.UPDATE_OK)
                        .build();

                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return null;
        }catch(BadCredentialsException e){
            response = GeneralResponse.builder()
                    .code(StatusCode.BAD_REQUEST)
                    .message(ResponseMessage.UPDATE_FAILED)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

//    @PreAuthorize()
    @PatchMapping("/status/{reservationId}")
    public ResponseEntity<?> updateReservationStatusByManager(@PathVariable Long reservationId, ReservationRequestDto reservationRequestDto){
        ReservationResponseDto dto = reservationService.ChangeStatusByManager(reservationId,reservationRequestDto.getReservationStatus());

        GeneralResponse response = GeneralResponse.builder()
                .code(StatusCode.OK)
                .message(ResponseMessage.UPDATE_OK)
                .data(dto)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
