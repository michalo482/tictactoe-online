package com.example.tictactoe3.repository;

import com.example.tictactoe3.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinRepository extends JpaRepository<Player, Long> {

    Player findByToken(String token);
    Player deleteByToken(String token);
}
