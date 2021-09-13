package com.example.tictactoe3.repository;

import com.example.tictactoe3.model.Join;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinRepository extends JpaRepository<Join, Long> {

    Join findByToken(String token);
    Join deleteByToken(String token);
}
