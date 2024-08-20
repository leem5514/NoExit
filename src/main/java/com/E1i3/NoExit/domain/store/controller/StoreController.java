package com.E1i3.NoExit.domain.store.controller;

import com.E1i3.NoExit.domain.common.dto.CommonResDto;
import com.E1i3.NoExit.domain.game.dto.GameResDto;
import com.E1i3.NoExit.domain.store.domain.Store;
import com.E1i3.NoExit.domain.store.dto.StoreResDto;
import com.E1i3.NoExit.domain.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/store/list")
    public ResponseEntity<?> storeList() {
        List<StoreResDto> stores = storeService.storeList();
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "OK", stores), HttpStatus.OK);
    }
}
