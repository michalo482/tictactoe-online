package com.example.tictactoe3.controller;

import com.example.tictactoe3.model.Board;
import com.example.tictactoe3.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

public class BoardController {

    @Autowired
    private BoardService boardService;

    @PostMapping("/board")
    public void createBoard() {
        Board board = new Board();
        boardService.addBoard(board);
    }
}
