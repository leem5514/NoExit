package com.E1i3.NoExit.domain.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.E1i3.NoExit.domain.notification.dto.NotificationResDto;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationResDto, Long> {
	List<NotificationResDto> findByEmail(String email);

}
