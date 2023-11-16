package com.sch.crane.cranewebbackend_v2.Service.Service;

import com.sch.crane.cranewebbackend_v2.Data.DTO.Reservation.ReservationRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Reservation.ReservationResponseDto;
import com.sch.crane.cranewebbackend_v2.Data.Repository.Reservation.ReservationRepository;
import com.sch.crane.cranewebbackend_v2.Data.Repository.User.UserRepository;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Reservation;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.ReservationStatus;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserRole;
import com.sch.crane.cranewebbackend_v2.Service.Exception.UserNameNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    //예약 생성
    @Transactional
    public ReservationResponseDto createReservation(String userEmail, ReservationRequestDto dto){
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(UserNameNotFoundException::new);

        //TODO: 해당 시간대 예약 조회하여 중복 체크하는 로직 필요.

        Reservation reservation = Reservation.builder()
                .resStartTime(dto.getResStartTime())
                .resEndTime(dto.getResEndTime())
                .reservationStatus(ReservationStatus.WAIT_APPROVAL)
                .user(user)
                .build();

        Reservation res = reservationRepository.save(reservation);

        return ReservationResponseDto.builder()
                .resStartTime(res.getResStartTime())
                .resEndTime(res.getResEndTime())
                .userName(res.getUser().getUsername())
                .uid(res.getUser().getUid())
                .reservationStatus(res.getReservationStatus())
                .build();
    }

    //개별 예약 읽기
    @Transactional
    public ReservationResponseDto readReservation(Long rid){
        Reservation reservation = reservationRepository.findById(rid).orElseThrow(EntityNotFoundException::new);

        return ReservationResponseDto.builder()
                .resStartTime(reservation.getResStartTime())
                .resEndTime(reservation.getResEndTime())
                .reservationStatus(reservation.getReservationStatus())
                .uid(reservation.getUser().getUid())
                .userName(reservation.getUser().getUsername())
                .build();
    }

    //사용자 별 예약 확인
    @Transactional
    public List<ReservationResponseDto> getReservationWithUserEmail(String userEmail){
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(UserNameNotFoundException::new);
        List<Reservation> reservation = reservationRepository.findByUserEmail(userEmail);
        List<ReservationResponseDto> dtoList = new ArrayList<>();

        for(Reservation r : reservation){
            ReservationResponseDto dto = ReservationResponseDto.builder()
                    .userName(r.getUser().getUsername())
                    .uid(r.getUser().getUid())
                    .reservationStatus(r.getReservationStatus())
                    .resStartTime(r.getResStartTime())
                    .resEndTime(r.getResEndTime())
                    .build();

            dtoList.add(dto);
        }

        return dtoList;
    }

    //주간 예약 목록
    @Transactional
    public List<ReservationResponseDto> getReservationOfWeekWithDate(LocalDate date){
        LocalDateTime fromTime = date.atTime(0,0);
        LocalDateTime toTime = date.atTime(23, 59);
        List<Reservation> reservationList = reservationRepository.findByResStartTimeBetween(fromTime, toTime);
        List<ReservationResponseDto> dtoList = new ArrayList<>();

        for(Reservation r : reservationList){
            ReservationResponseDto dto = ReservationResponseDto.builder()
                    .userName(r.getUser().getUsername())
                    .uid(r.getUser().getUid())
                    .resStartTime(r.getResStartTime())
                    .resEndTime(r.getResEndTime())
                    .reservationStatus(r.getReservationStatus())
                    .build();

            dtoList.add(dto);
        }

        return dtoList;
    }


    //예약 수정
    @Transactional
    public ReservationResponseDto updateReservation(String userEmail, ReservationRequestDto dto){
        Reservation reservation = reservationRepository.findById(dto.getRid()).orElseThrow(EntityNotFoundException::new);
        User RequestUser = userRepository.findByUserEmail(userEmail).orElseThrow(EntityNotFoundException::new);
        UserRole Role = RequestUser.getUserRole();

        //요청자가 예약 생성자, 사이트 관리자, 임원인 경우에만 수정 가능.
        if(reservation.getUser() == RequestUser ||
                Role == UserRole.ADMIN ||
                Role == UserRole.MANAGER) {

            reservation.updateReservation(dto.getResStartTime(), dto.getResEndTime());

            Reservation res = reservationRepository.save(reservation);

            return ReservationResponseDto.builder()
                    .userName(res.getUser().getUsername())
                    .uid(res.getUser().getUid())
                    .reservationStatus(res.getReservationStatus())
                    .resStartTime(res.getResStartTime())
                    .resEndTime(res.getResEndTime())
                    .build();
        }else{
            throw new BadCredentialsException("권한이 없는 사용자입니다.");
        }



    }



    //예약 삭제(승인 전에만 가능)
    @Transactional
    public boolean deleteReservation(String userEmail, Long rid){
        Reservation reservation = reservationRepository.findById(rid).orElseThrow(EntityNotFoundException::new);
        User RequestUser = userRepository.findByUserEmail(userEmail).orElseThrow(EntityNotFoundException::new);
        UserRole Role = RequestUser.getUserRole();

        //요청자가 예약 생성자, 사이트 관리자, 임원인 경우에만 삭제 가능.
        if(reservation.getUser() == RequestUser || Role == UserRole.ADMIN || Role == UserRole.MANAGER) {
            //승인 상태 이전에만 삭제 가능
            if(reservation.getReservationStatus() != ReservationStatus.WAIT_APPROVAL){
                throw new IllegalStateException("Cannot Delete reservation in this state.");
            }else{
                reservationRepository.deleteById(rid);
                return true;
            }
        }else {
            throw new BadCredentialsException("권한이 없는 사용자입니다.");
        }
    };


    //예약 상태 변경 (관리자)
    @Transactional
    public ReservationResponseDto ChangeStatusByManager(Long rid, ReservationStatus status){
        Reservation reservation = reservationRepository.findById(rid).orElseThrow(EntityNotFoundException::new);

        reservation.updateReservationStatus(status);
        reservationRepository.save(reservation);

        return ReservationResponseDto.builder()
                .resStartTime(reservation.getResStartTime())
                .resEndTime(reservation.getResEndTime())
                .reservationStatus(status)
                .build();

    }




}
