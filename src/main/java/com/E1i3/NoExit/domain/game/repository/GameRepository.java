package com.E1i3.NoExit.domain.game.repository;

import com.E1i3.NoExit.domain.game.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

}
