package com.E1i3.NoExit.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.E1i3.NoExit.domain.store.domain.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store,Long> {
}
