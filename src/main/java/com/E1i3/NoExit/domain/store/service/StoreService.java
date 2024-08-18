package com.E1i3.NoExit.domain.store.service;

import com.E1i3.NoExit.domain.store.domain.Store;
import com.E1i3.NoExit.domain.store.dto.StoreResDto;
import com.E1i3.NoExit.domain.store.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Transactional
@Service
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public List<StoreResDto> storeList() {

        List<Store> storeList = storeRepository.findAll();
        List<StoreResDto> storeResDtosList = new ArrayList<>();

        for (Store store : storeList){
            storeResDtosList.add(store.fromEntity());
        }

        return storeResDtosList;
    }

}
