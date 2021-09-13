package com.example.tictactoe3.repository;

import com.example.tictactoe3.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer> {

}
