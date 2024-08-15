package com.E1i3.NoExit.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.E1i3.NoExit.domain.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
