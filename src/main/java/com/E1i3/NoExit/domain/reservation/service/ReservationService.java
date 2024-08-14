package com.E1i3.NoExit.domain.reservation.service;

import com.E1i3.NoExit.domain.common.domain.DelYN;

import com.E1i3.NoExit.domain.game.domain.Game;
import com.E1i3.NoExit.domain.game.repository.GameRepository;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.domain.Role;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import com.E1i3.NoExit.domain.notification.service.NotificationService;
import com.E1i3.NoExit.domain.owner.domain.Owner;
import com.E1i3.NoExit.domain.owner.repository.OwnerRepository;
import com.E1i3.NoExit.domain.reservation.domain.ApprovalStatus;
import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.reservation.dto.ReservationDetailResDto;
import com.E1i3.NoExit.domain.reservation.dto.ReservationListResDto;
import com.E1i3.NoExit.domain.reservation.dto.ReservationSaveDto;
import com.E1i3.NoExit.domain.reservation.dto.ReservationUpdateResDto;
import com.E1i3.NoExit.domain.reservation.repository.ReservationRepository;
import com.E1i3.NoExit.domain.store.domain.Store;
import com.E1i3.NoExit.domain.store.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired

    private StoreRepository storeRepository;

    private final NotificationService notificationService;
    @Autowired
    @Qualifier("2")
    private RedisTemplate<String, Object> reservationRedisTemplate;

    private static final String RESERVATION_LOCK_PREFIX = "reservation:lock:";

    public ReservationService(ReservationRepository reservationRepository, GameRepository gameRepository, MemberRepository memberRepository, OwnerRepository ownerRepository, StoreRepository storeRepository,
		NotificationService notificationService) {
        this.reservationRepository = reservationRepository;
        this.gameRepository = gameRepository;
        this.memberRepository = memberRepository;
        this.ownerRepository = ownerRepository;
        this.storeRepository = storeRepository;
		    this.notificationService = notificationService;
	  }

    /* 레디스를 통한 예약하기 */
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public Reservation save(ReservationSaveDto dto) {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Authenticated user email: {}", memberEmail);

        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> {
                    log.error("Invalid email: {}", memberEmail);
                    return new IllegalArgumentException("없는 이메일입니다.");
                });
        Game game = gameRepository.findById(dto.getGameId())
                .orElseThrow(() -> {
                    log.error("Invalid game ID: {}", dto.getGameId());
                    return new IllegalArgumentException("없는 게임 ID입니다.");
                });

        String reservationKey = RESERVATION_LOCK_PREFIX + game.getStore().getStoreName() + ":" + game.getGameName() + ":" + dto.getResDate() + ":" + dto.getResDateTime();
        log.debug("Reservation key: {}", reservationKey);

        if (Boolean.TRUE.equals(reservationRedisTemplate.hasKey(reservationKey))) {
            log.error("Reservation conflict for key: {}", reservationKey);
            throw new IllegalStateException("(예약불가) 이미 예약 중인 시간대 입니다.");
        }

        // 예약은 30분 이내의 상태 변화가 없다면 자동으로 삭제
        reservationRedisTemplate.opsForValue().set(reservationKey, "LOCKED", 3, TimeUnit.HOURS);

        try {
            Reservation reservation = dto.toEntity(member, game);
            log.debug("Saving reservation: {}", reservation);
            reservationRepository.save(reservation);
            notificationService.notifyResToOwner(dto);
            reservationRedisTemplate.opsForValue().set(reservationKey, "RESERVED", 3, TimeUnit.HOURS); // 3시간 뒤 자동 삭제

            return reservation;
        } catch (Exception e) {
            log.error("Exception occurred during reservation save: ", e);
            reservationRedisTemplate.delete(reservationKey);
            throw e;
        }
    }
    /* 예약한 리스트 */
    @PreAuthorize("hasRole('USER')")
    public List<ReservationListResDto> find() {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        List<Reservation> reservations = reservationRepository.findByMemberEmailAndDelYN(member.getEmail(), DelYN.N);

        return reservations.stream()
                .map(ReservationListResDto::listFromEntity)
                .collect(Collectors.toList());
    }


    /* 예약 상세 보기 */
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public ReservationDetailResDto getReservationDetail(Long id) {
        // 로그인된 사용자의 이메일을 가져옴
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        // 이메일을 통해 회원 정보 조회
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        if (reservation.getDelYN() != DelYN.N) {
            throw new IllegalArgumentException("삭제된 예약입니다.");
        }

        if (!reservation.getMember().equals(member)) {
            throw new IllegalArgumentException("본인의 예약만 조회할 수 있습니다.");
        }

        return reservation.toDetailDto();
    }
    /* 예약 내역 조회 (사장님용) */
    @Transactional
    public List<ReservationDetailResDto> findReservationsByOwner() {
        // 현재 인증된 사용자의 이메일 가져오기
        String ownerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("DEBUG: Owner Email = " + ownerEmail);

        // Owner 조회
        Owner owner = ownerRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> {
                    System.out.println("ERROR: Owner not found with email = " + ownerEmail);
                    return new IllegalArgumentException("등록되지 않은 점장 이메일입니다.");
                });
        System.out.println("DEBUG: Owner found = " + owner);

        List<Store> stores = storeRepository.findByOwner(owner);
        // Owner가 관리하는 모든 게임들을 가져옴
        List<Game> games = gameRepository.findByStoreIn(stores);
        System.out.println("DEBUG: Games found = " + games);

        // 해당 게임들에 대한 예약들을 모두 가져옴
        List<Reservation> reservations = reservationRepository.findByGameIn(games);
        System.out.println("DEBUG: Reservations found = " + reservations);

        // DTO로 변환하여 반환
        return reservations.stream()
                .map(Reservation::toDetailDto)
                .peek(dto -> System.out.println("DEBUG: ReservationDetailResDto = " + dto))
                .collect(Collectors.toList());
    }


    /* 추가) 후 STORE 컬럼과 접근하여 STORE_ID의 GAME_ID을 받아서 처리 예정  */
//    @PreAuthorize("hasRole('OWNER')")
    @Transactional
    public Reservation updateApprovalStatus(ReservationUpdateResDto dto) {
        try {
            // 1. 점주 이메일 가져오기 및 점주 확인
            String ownerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            log.debug("Authenticated owner email: {}", ownerEmail);

            Owner owner = ownerRepository.findByEmail(ownerEmail)
                    .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 점장 이메일입니다."));

            // 2. 게임 ID로 게임 조회
            Game game = gameRepository.findById(dto.getGameId())
                    .orElseThrow(() -> new IllegalArgumentException("없는 게임 ID입니다."));

            log.debug("Game found: {}", game.getGameName());

//            // 3. 점주가 이 게임에 대한 권한이 있는지 확인
//            if (!game.getStore().getOwner().equals(owner)) {
//                throw new IllegalArgumentException("해당 예약을 처리할 권한이 없습니다.");
//            }

            // 4. Redis 키 생성 및 로그 출력
            String reservationKey = RESERVATION_LOCK_PREFIX + game.getStore().getStoreName() + ":" + game.getGameName() + ":" + dto.getResDate() + ":" + dto.getResDateTime();
            log.debug("Reservation key: {}", reservationKey);

            // 5. 예약 조회
            Optional<Reservation> optionalReservation = reservationRepository.findByGameAndResDateAndResDateTime(game, LocalDate.parse(dto.getResDate()), dto.getResDateTime());
            if (optionalReservation.isEmpty()) {
                throw new IllegalArgumentException("해당 시간대에 예약한 손님이 없습니다.");
            }

            Reservation reservation = optionalReservation.get();

            // 6. 예약 상태 업데이트
            reservation.updateStatus(dto.getApprovalStatus());

            // 7. Redis에 예약 상태 저장 또는 삭제
            if (dto.getApprovalStatus() == ApprovalStatus.OK) {
                reservationRedisTemplate.opsForValue().set(reservationKey, "RESERVED"); // 예약 확정 시 해당 시간대를 영구적으로 예약 불가 처리
            } else if (dto.getApprovalStatus() == ApprovalStatus.NO) {
                // 예약 거절 시 해당 시간대를 다시 예약 가능하도록 처리
                reservationRedisTemplate.delete(reservationKey);
            }

            return reservationRepository.save(reservation);
        } catch (Exception e) {
            log.error("Error during approval status update: ", e);
            throw e;
        }
    }




    /* 예약취소 */
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public Reservation cancelReservation(Long reservationId) {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("예약을 찾을 수 없습니다."));

        if (!reservation.getMember().equals(member)) {
            throw new IllegalArgumentException("본인의 예약만 취소할 수 있습니다.");
        }

        reservation.updateDelYN();
        reservationRepository.save(reservation);

        String reservationKey = RESERVATION_LOCK_PREFIX + reservation.getGame().getStore().getStoreName() + ":" + reservation.getGame().getGameName() + ":" + reservation.getResDate() + ":" + reservation.getResDateTime();
        reservationRedisTemplate.delete(reservationKey);

        return reservation;
    }

//    @PreAuthorize("hasRole('USER')")
//    public Optional<Reservation> findReservationTime(String resDate, String resDateTime) {
//        String reservationKey = RESERVATION_LOCK_PREFIX + resDate + ":" + resDateTime;
//
//        if (Boolean.TRUE.equals(reservationRedisTemplate.hasKey(reservationKey))) {
//            return reservationRepository.findByResDateAndResDateTime(LocalDate.parse(resDate), resDateTime);
//        } else {
//            return Optional.empty();
//        }
//    }

}
