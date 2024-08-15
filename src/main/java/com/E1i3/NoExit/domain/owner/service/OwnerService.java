package com.E1i3.NoExit.domain.owner.service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.E1i3.NoExit.domain.common.service.RedisService;
import com.E1i3.NoExit.domain.common.service.S3Service;
import com.E1i3.NoExit.domain.game.domain.Game;
import com.E1i3.NoExit.domain.game.repository.GameRepository;
import com.E1i3.NoExit.domain.owner.domain.Owner;
import com.E1i3.NoExit.domain.owner.dto.OwnerDetResDto;
import com.E1i3.NoExit.domain.owner.dto.OwnerListResDto;
import com.E1i3.NoExit.domain.owner.dto.OwnerSaveReqDto;
import com.E1i3.NoExit.domain.owner.dto.OwnerUpdateDto;
import com.E1i3.NoExit.domain.owner.repository.OwnerRepository;
import com.E1i3.NoExit.domain.reservation.dto.ReservationSaveDto;
import com.E1i3.NoExit.domain.store.domain.Store;
import com.E1i3.NoExit.domain.store.repository.StoreRepository;


@Service
public class OwnerService{

	private final OwnerRepository ownerRepository;
	private final RedisService redisService;
	private final PasswordEncoder passwordEncoder;
	private final S3Service s3Service;
	private final GameRepository gameRepository;


	private static final String AUTH_EMAIL_PREFIX = "EMAIL_CERTIFICATE : ";

	@Autowired
	public OwnerService(OwnerRepository ownerRepository,
		RedisService redisService, PasswordEncoder passwordEncoder,
		S3Service s3Service, GameRepository gameRepository) {
		this.ownerRepository = ownerRepository;
		this.redisService = redisService;
		this.passwordEncoder = passwordEncoder;
		this.s3Service = s3Service;
		this.gameRepository = gameRepository;
	}

	@Transactional
	public Owner ownerCreate(OwnerSaveReqDto ownerSaveReqDto, MultipartFile imgFile) {
		// 레디스에 인증이 된 상태인지 확인
		String chkVerified = redisService.getValues(AUTH_EMAIL_PREFIX + ownerSaveReqDto.getEmail());
		System.out.println(chkVerified);

		if (chkVerified == null || !chkVerified.equals("true")) {
			throw new IllegalStateException("이메일 인증이 필요합니다.");
		}

		// 이메일로 회원을 검색
		ownerRepository.findByEmail(ownerSaveReqDto.getEmail()).ifPresent(existingMember -> {
			throw new EntityExistsException("이미 존재하는 이메일입니다.");
		});
		String encodedPassword = passwordEncoder.encode(ownerSaveReqDto.getPassword());
		String imageUrl = s3Service.uploadFile(imgFile, "owner");

		return ownerRepository.save(ownerSaveReqDto.toEntity(encodedPassword,  imageUrl));
	}

	// 회원 리스트 조회?
	public Page<OwnerListResDto> ownerList(Pageable pageable) {
		Page<Owner> memberList = ownerRepository.findAll(pageable);
		return memberList.map(a -> a.fromEntity());
	}

	// 회원 삭제
	@Transactional
	public Owner ownerDelete(String email) {
		Owner owner = ownerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));
		return owner.updateDelYN();
	}

	// 회원 정보 수정
	@Transactional
	public Owner ownerUpdate(OwnerUpdateDto ownerUpdateDto) {
		Owner owner = ownerRepository.findByEmail(ownerUpdateDto.getEmail()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));
		String encodedPassword = passwordEncoder.encode(ownerUpdateDto.getPassword());
		return owner.updateOwner(ownerUpdateDto, encodedPassword);
	}

	public String getEmailFromToken() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	// 회원 상세 조회
	public OwnerDetResDto myInfo() {
		String ownerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		Owner owner = ownerRepository.findByEmail(ownerEmail).orElseThrow(EntityNotFoundException::new);
		return owner.detFromEntity();
	}

	public void changeReservationStatus(ReservationSaveDto reservationSaveDto){
		// 	예약 상태 변경
	}

	public String getEmailFromReservation(ReservationSaveDto dto) {
		Game game = gameRepository.findById(dto.getGameId())
			.orElseThrow(() -> new EntityNotFoundException("일치하는 게임이 존재하지 않습니다."));

		Store store = game.getStore();
		return store.getOwner().getEmail();
	}
}

