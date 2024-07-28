package com.E1i3.NoExit.domain.game.repository;

import com.E1i3.NoExit.domain.game.domain.Game;
import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

}
