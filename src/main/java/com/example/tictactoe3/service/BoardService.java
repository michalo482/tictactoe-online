package com.example.tictactoe3.service;

import com.example.tictactoe3.model.Board;
import com.example.tictactoe3.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> getAllBoards() {
        List<Board> boards = new ArrayList<>();
        boardRepository.findAll().forEach(boards::add);
        return boards;
    }

    public Board getBoard(Integer id) {
        return boardRepository.findById(id).orElseThrow();
    }

    public void addBoard(Board board) {
        boardRepository.save(board);
    }
    public void updateBoard(Board board) {
        boardRepository.save(board);
    }

}
