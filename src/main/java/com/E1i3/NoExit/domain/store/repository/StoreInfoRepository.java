package com.E1i3.NoExit.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.E1i3.NoExit.domain.store.domain.Store;

public interface StoreInfoRepository extends JpaRepository<Store,Long> {
}
