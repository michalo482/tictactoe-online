package com.example.tictactoe3.repository;

import com.example.tictactoe3.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer> {

}
