package com.E1i3.NoExit.domain.store.repository;

import com.E1i3.NoExit.domain.owner.domain.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import com.E1i3.NoExit.domain.store.domain.Store;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store,Long> {
    List<Store> findByOwner(Owner owner);
    List<Store> findAll();
}
