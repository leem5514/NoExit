package com.E1i3.NoExit.domain.reservation.service;

import com.E1i3.NoExit.domain.game.domain.Game;
import com.E1i3.NoExit.domain.game.repository.GameRepository;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.domain.Role;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import com.E1i3.NoExit.domain.reservation.domain.ApprovalStatus;
import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.reservation.domain.ReservationStatus;
import com.E1i3.NoExit.domain.reservation.dto.ReservationDetailResDto;
import com.E1i3.NoExit.domain.reservation.dto.ReservationListResDto;
import com.E1i3.NoExit.domain.reservation.dto.ReservationSaveDto;
import com.E1i3.NoExit.domain.reservation.dto.ReservationUpdateResDto;
import com.E1i3.NoExit.domain.reservation.repository.ReservationRepository;
import lombok.extern.slf4j.XSlf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    //private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GameRepository gameRepository;

    public ReservationService(ReservationRepository reservationRepository, GameRepository gameRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.gameRepository = gameRepository;
        this.memberRepository = memberRepository;
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String RESERVATION_LOCK_PREFIX = "reservation:lock:";

    /* 예약하기 */
//    @Transactional
//    public Reservation save(ReservationSaveDto dto) {
//        Member member = memberRepository.findByEmail(dto.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("없는 이메일입니다."));
//        Game game = gameRepository.findById(dto.getGameId())
//                .orElseThrow(() -> new IllegalArgumentException("없는 게잉 아이디입니다. 후 게임 이름으로 변경"));
//
//        Reservation reservation = dto.toEntity(member, game);
//        System.out.println(reservation.getReservationUuid());
//        return reservationRepository.save(reservation);
//    }
//    public List<ReservationListResDto> list(String email) {
//        List<Reservation> reservations = reservationRepository.findByMemberEmail(email);
//        return reservations.stream()
//                .map(ReservationListResDto::listFromEntity)
//                .collect(Collectors.toList());
//    }
    /* 레디스를 통한 예약하기 */
    @Transactional
    public Reservation save(ReservationSaveDto dto) {
        String reservationKey = RESERVATION_LOCK_PREFIX + dto.getResDate() + ":" + dto.getResDateTime();

        if (Boolean.TRUE.equals(redisTemplate.hasKey(reservationKey))) {
            throw new IllegalStateException("선택하신 시간대에 대기자가 있습니다. 예약불가");
        }

        redisTemplate.opsForValue().set(reservationKey, "LOCKED", 5, TimeUnit.MINUTES);

        try {
            Member member = memberRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("없는 이메일입니다."));
            Game game = gameRepository.findById(dto.getGameId())
                    .orElseThrow(() -> new IllegalArgumentException("없는 게잉id 입니다.")); // 후 ) 게임 이름으로 반환객체 변경

            Reservation reservation = dto.toEntity(member, game);
            System.out.println(reservation.getReservationUuid());
            reservationRepository.save(reservation);

            redisTemplate.opsForValue().set(reservationKey, "RESERVED");

            return reservation;
        } catch (Exception e) {
            redisTemplate.delete(reservationKey);
            throw e;
        }
    }

    /* email을 통한 예약 조회(USER 기준) */
    @Transactional(readOnly = true)
    public List<ReservationListResDto> find(String email) {
        List<Reservation> reservations = reservationRepository.findByMemberEmail(email);
        return reservations.stream()
                .map(ReservationListResDto::listFromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public Reservation updateApprovalStatus(ReservationUpdateResDto dto) {
        Member admin = memberRepository.findByEmail(dto.getAdminEmail())
                .orElseThrow(() -> new IllegalArgumentException("ADMIN 이메일이 아닙니다."));

        if (admin.getRole() != Role.ADMIN) {
            throw new IllegalStateException("ADMIN만 approval을 업데이트 가능함");
        }

        Game game = gameRepository.findById(dto.getGameId())
                .orElseThrow(() -> new IllegalArgumentException("없는 게임 ID입니다."));

        String reservationKey = RESERVATION_LOCK_PREFIX + dto.getResDate() + ":" + dto.getResDateTime();
        Optional<Reservation> optionalReservation = reservationRepository.findByGameAndResDateAndResDateTime(game, LocalDate.parse(dto.getResDate()), dto.getResDateTime());
        if (optionalReservation.isEmpty()) {
            throw new IllegalArgumentException("Invalid redis key");
        }

        Reservation reservation = optionalReservation.get();
        reservation.updateStatus(dto.getApprovalStatus());

        return reservationRepository.save(reservation);
    }
    @Transactional
    public ReservationDetailResDto getReservationDetail(Long id) {
        Optional<Reservation> findReservation = reservationRepository.findById(id);
        Reservation reservation = findReservation.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
        return reservation.toDetailDto();
    }

//    @Transactional
//    public Reservation updateApprovalStatus(UUID reservationUuid, String adminEmail, ApprovalStatus status) {
//        Member admin = memberRepository.findByEmail(adminEmail).orElseThrow(() -> new IllegalArgumentException("Invalid email"));
//        if (admin.getRole() != Role.ADMIN) {
//            throw new IllegalStateException("ROLE 이 ADMIN만 수정가능합니다.");
//        }
//        Reservation reservation = reservationRepository.findByReservationUuid(reservationUuid)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation UUID"));
//
//        reservation.setApprovalStatus(status);
//        if (status == ApprovalStatus.OK) {
//            reservation.setReservationStatus(ReservationStatus.ACCEPT);
//        } else if (status == ApprovalStatus.NO) {
//            reservation.setReservationStatus(ReservationStatus.REJECT);
//        }
//        return reservationRepository.save(reservation);
//    }

//    @Transactional
//    public Reservation updateApprovalStatus(ReservationUpdateResDto dto) {
//        Member admin = memberRepository.findByEmail(dto.getAdminEmail())
//                .orElseThrow(() -> new IllegalArgumentException("ADMIN 이메일이 아닙니다."));
//
//        if (admin.getRole() != Role.ADMIN) {
//            throw new IllegalStateException("Only ADMIN can update the approval status");
//        }
//
//        Optional<Reservation> optionalReservation = reservationRepository.findByReservationUuid(dto.getReservationUuid());
//        if (optionalReservation.isEmpty()) {
//            throw new IllegalArgumentException("Invalid reservation UUID");
//        }
//        Reservation reservation = optionalReservation.get();
//        reservation.updateStatus(dto.getApprovalStatus());
//
//        return reservationRepository.save(reservation);
//    }

//
//    /* 예약 상세 보기*/
//    @Transactional
//    public ReservationDetailResDto getReservationDetail(Long id) {
//        Optional<Reservation> findReservation = reservationRepository.findById(id);
//        Reservation reservation = findReservation.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
//        ReservationDetailResDto reservationDetailResDto = reservation.detailFromEntity();
//        return reservationDetailResDto;
//    }
//
//    /* 예약 삭제 */
//    @Transactional
//    public void delete(Long id) {
//        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("예약내역이 없음"));
////        if (!Reservation.getMember().getEmail().equals(email)) {
////            throw new IllegalArgumentException("나의 예약x");
////        }
//        reservationRepository.delete(reservation);
//    }

}
