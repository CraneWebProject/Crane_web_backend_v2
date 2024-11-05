package com.sch.crane.cranewebbackend_v2.Service.Service;

import com.sch.crane.cranewebbackend_v2.Data.DTO.Reservation.ReservationRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Reservation.ReservationResponseDto;
import com.sch.crane.cranewebbackend_v2.Data.Repository.Instrument.InstrumentRepository;
import com.sch.crane.cranewebbackend_v2.Data.Repository.Reservation.ReservationRepository;
import com.sch.crane.cranewebbackend_v2.Data.Repository.Team.TeamRepository;
import com.sch.crane.cranewebbackend_v2.Data.Repository.User.UserRepository;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Instrument;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Reservation;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Team;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.ReservationStatus;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserRole;
import com.sch.crane.cranewebbackend_v2.Service.Exception.UserNameNotFoundException;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final InstrumentRepository instrumentRepository;
    private final TeamRepository teamRepository;


    //Batch 를 통해 미리 1주간 예약을 생성해 둠.
    //예약 신청시 생성된 예약에 user, team 등 정보를 추가하고 예약 가능 상태를 불가능으로 바꿈.
    //합주 신청시 해당 시간 장비 사용 가능을 불가능으로 바꿈.
    //밤 11시에 배치로 다음주 예약 생성
    //밤 12시에 다음주 합주 신청 open
    //낮 12시에 다음주 장비 신청 open

    //초기 예약 생성
    //오늘부터 1주일간의 예약을 모두 생성함.
    //예약이 없는경우 실행되도록 할 것.
    @Transactional
    public void initReservation(){
        LocalDate date = LocalDate.now();
        List<ReservationResponseDto> dtoList = findResByDayAndInst(date, 1L);

        if(dtoList.isEmpty()){
            for(int i = 0; i < 8; i++){
                createReservationAfterNDays(i);
                openEnsembleAfterNDays(i);
                openInstAfterNDays(i);
            }
        }else{
            return;
        }
    }

    //예약 생성
    //n일 이후 예약 전체 생성
    @Transactional
    public void createReservationAfterNDays(int n){
        // n일 후 날짜 계산
        LocalDateTime reservationDate = LocalDateTime.now().plusDays(n).withHour(0).withMinute(0);

        // 24시간 동안 30분 단위로 예약 생성
        for (int hour = 8; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                LocalDateTime startTime = reservationDate.withHour(hour).withMinute(minute);

                // 각 Instrument 마다 예약 생성
                List<Instrument> instruments = instrumentRepository.findAll();
                for (Instrument instrument : instruments) {
                    Reservation reservation = Reservation.builder()
                            .resName(null)
                            .resStartTime(startTime)
                            .reservationStatus(null)
                            .resPossible(false)
                            .user(null)
                            .team(null)
                            .instrument(instrument)
                            .build();

                    reservationRepository.save(reservation);
                }
            }
        }
    }
    //n일 뒤 합주 예약 open
    @Transactional
    public void openEnsembleAfterNDays(int n){
        LocalDateTime startOfDay = LocalDateTime.now().plusDays(n).withHour(0).withMinute(0);
        LocalDateTime endOfDay = LocalDateTime.now().plusDays(n).withHour(23).withMinute(59);

        List<Reservation> resList = reservationRepository.findAllByDate(startOfDay, endOfDay);

        for(Reservation r : resList){
            if(r.getInstrument().getInstName().equals("합주")){
                r.updateReservation(r.getResName(), true, r.getReservationStatus(), r.getUser(), r.getTeam());
                reservationRepository.save(r);
            }
        }
    }

    //n일 뒤 악기 예약 open
    @Transactional
    public void openInstAfterNDays(int n){
        LocalDateTime startOfDay = LocalDateTime.now().plusDays(n).withHour(0).withMinute(0);
        LocalDateTime endOfDay = LocalDateTime.now().plusDays(n).withHour(23).withMinute(59);

        List<Reservation> resList = reservationRepository.findAllByDate(startOfDay, endOfDay);

        for(Reservation r : resList){
            if(!r.getInstrument().getInstName().equals("합주")){
                r.updateReservation(r.getResName(), true, r.getReservationStatus(), r.getUser(), r.getTeam());
                reservationRepository.save(r);
            }
        }
    }

    //단일 예약 생성
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto dto){
        //악기 검색
        Instrument inst = instrumentRepository.findById(dto.getIid()).orElseThrow(EntityNotFoundException::new);
        if(inst.getInstUsable() == false){
            throw new EntityExistsException("예약 불가 장비");
        }
        Reservation reservation = reservationRepository.findByResStartTimeAndInstrument(dto.getResStartTime(), inst);
        if(reservation == null || !reservation.getResPossible() ){
            throw new EntityNotFoundException("예약 불가");
        }

        reservation.updateReservation(
                dto.getResName(),
                false,
                ReservationStatus.APPROVED,
                null,
                null
        );
        reservationRepository.save(reservation);

        return new ReservationResponseDto(reservation);
    }

    //예약 확인
    @Transactional(readOnly = true)
    public ReservationResponseDto getReservation(Long rid){
        Reservation res = reservationRepository.findById(rid).orElseThrow(EntityNotFoundException::new);
        return new ReservationResponseDto(res);
    }

    //예약 수정
    //예약명, 예약 상태 수정 가능
    //신청자 혹은 관리자, 매니저만 수정 가능
    @Transactional
    public ReservationResponseDto updateReservation(String email, ReservationRequestDto dto){
        User user = userRepository.findByUserEmail(email).orElseThrow(EntityNotFoundException::new );
        Team team = teamRepository.findById(dto.getTid()).orElseThrow(EntityNotFoundException::new );
        Reservation res = reservationRepository.findById(dto.getRid()).orElseThrow(EntityNotFoundException::new);

        if (!user.getUid().equals(res.getUser().getUid()) &&
                !(user.getUserRole() == UserRole.ROLE_ADMIN || user.getUserRole() == UserRole.ROLE_MANAGER)) {
            throw new BadCredentialsException("권한이 없는 사용자");
        }

        if(dto.getResName() == null){
            dto.setResName(res.getResName());
        }

        if(dto.getReservationStatus() == null){
            dto.setReservationStatus(res.getReservationStatus());
        }

        if(dto.getTid() == null){
            team = null;
        }

        res.updateReservation( dto.getResName(),
                               res.getResPossible(),
                               dto.getReservationStatus(),
                               user,
                               team
                           );
        reservationRepository.save(res);

        return new ReservationResponseDto(res);
    }

    //예약하기
    @Transactional
    public ReservationResponseDto makeAReservation(String userEmail, Long rid){
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(EntityNotFoundException::new);
        Reservation res = reservationRepository.findById(rid).orElseThrow(EntityNotFoundException::new);

        if(!res.getResPossible()){
            throw new IllegalStateException("이미 예약된 시간");
        }

        //합주 예약인 경우, 장비 예약 불가 처리
        List<Reservation> resList = reservationRepository.findByResStartTime(res.getResStartTime());
        if(res.getInstrument().getInstName().equals("합주") ) {

            for (Reservation r : resList) {
                r.updateReservation(
                        r.getResName(),
                        false,
                        res.getReservationStatus(),
                        null,
                        null
                );
                reservationRepository.save(r);
            }
        }else{
            //장비 예약인 경우, 합주 예약 불가 처리
            for(Reservation r : resList){
                if(r.getInstrument().getInstName().equals("합주")){
                    r.updateReservation(
                            r.getResName(),
                            false,
                            r.getReservationStatus(),
                            null,
                            null

                    );
                    reservationRepository.save(r);
                }
            }
        }

        res.updateReservation(
                res.getResName(),
                false,
                ReservationStatus.WAIT_APPROVAL,
                user,
                null
        );
        System.out.println("예약 완료. 예약자 : " + user.getUserEmail());
        reservationRepository.save(res);
        return new ReservationResponseDto(res);
    }

    //예약 삭제
    //추후 batch 로 구현 예정

    //예약 취소
    //신청자 혹은 관리자, 매니저만 취소 가능
    public ReservationResponseDto cancelReservation(String userEmail, Long rid){
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(EntityNotFoundException::new);
        Reservation res = reservationRepository.findById(rid).orElseThrow(EntityNotFoundException::new);

        if (!user.getUid().equals(res.getUser().getUid()) &&
                !(user.getUserRole() == UserRole.ROLE_ADMIN || user.getUserRole() == UserRole.ROLE_MANAGER)) {
            throw new BadCredentialsException("권한이 없는 사용자");
        }

        //합주 예약 취소인경우, 장비 예약 허용으로 변경
        List<Reservation> resList =  reservationRepository.findByResStartTime(res.getResStartTime());
        if(res.getInstrument().getInstName().equals("합주")){
            for(Reservation r : resList){
                r.updateReservation(
                        r.getResName(),
                        true,
                        res.getReservationStatus(),
                        null,
                        null);
                reservationRepository.save(r);

            }
        }else{
            //장비 예약 취소인 경우, 합주 예약 가능하도록 변경
            for(Reservation r : resList){
                if(r.getInstrument().getInstName().equals("합주")){
                    r.updateReservation(
                            null,
                            true,
                            null,
                            null,
                            null
                    );
                    reservationRepository.save(r);
                }
            }
        }

        res.cancelReservation();
        reservationRepository.save(res);
        return new ReservationResponseDto(res);
    }

    //일간 예약 목록
    @Transactional(readOnly = true)
    public List<ReservationResponseDto> findResByDayAndInst(LocalDate date, Long iid){
        Instrument inst = instrumentRepository.findById(iid).orElseThrow(EntityNotFoundException::new);

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        List<Reservation> reservationList = reservationRepository.findAllByResStartDateAndInstrument(startOfDay, endOfDay, iid);

        List<ReservationResponseDto> resDtoList = new ArrayList<>();

        for(Reservation r : reservationList){
            ReservationResponseDto dto =  new ReservationResponseDto(r);
            resDtoList.add(dto);
        }

        return resDtoList;
    }


    //사용자 별 예약목록
    @Transactional(readOnly = true)
    public List<ReservationResponseDto> findResByUser(Long uid){
        User user = userRepository.findById(uid).orElseThrow(EntityNotFoundException::new);
        List<Reservation> reservationList = reservationRepository.findByUid(uid);

        List<ReservationResponseDto> resDtoList = new ArrayList<>();

        for(Reservation r : reservationList){
            ReservationResponseDto dto = new ReservationResponseDto(r);
            resDtoList.add(dto);
        }

        return resDtoList;
    }


    //팀별 예약목록


}
