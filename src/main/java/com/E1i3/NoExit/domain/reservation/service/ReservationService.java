package com.E1i3.NoExit.domain.reservation.service;

import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.reservation.dto.ReservationDetailResDto;
import com.E1i3.NoExit.domain.reservation.dto.ReservationListResDto;
import com.E1i3.NoExit.domain.reservation.dto.ReservationSaveDto;
import com.E1i3.NoExit.domain.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /* 예약하기 */
    @Transactional
    public Reservation save(ReservationSaveDto dto) {
        Reservation reservation = dto.toEntity();
        return reservationRepository.save(reservation);
    }

    /* member 이메일에 따른 예약 리스트들 보기 */
    @Transactional(readOnly = true)
    public List<ReservationListResDto> getReservationsByMemberEmail(String email) {
        List<Reservation> reservations = reservationRepository.findByMemberEmail(email);
        return reservations.stream()
                .map(Reservation::listFromEntity)
                .collect(Collectors.toList());
    }

    /* 예약 상세 보기*/
    @Transactional
    public ReservationDetailResDto getReservationDetail(Long id) {
        Optional<Reservation> findReservation = reservationRepository.findById(id);
        Reservation reservation = findReservation.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
        ReservationDetailResDto reservationDetailResDto = reservation.detailFromEntity();
        return reservationDetailResDto;
    }

    /* 예약 삭제 */
    @Transactional
    public void delete(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("예약내역이 없음"));
//        if (!Reservation.getMember().getEmail().equals(email)) {
//            throw new IllegalArgumentException("나의 예약x");
//        }
        reservationRepository.delete(reservation);
    }

}
