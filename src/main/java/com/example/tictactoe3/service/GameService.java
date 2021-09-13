package com.example.tictactoe3.service;

import com.example.tictactoe3.model.Game;
import com.example.tictactoe3.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {

    @Autowired
    private final GameRepository gameRepository;

    public List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();
        games.addAll(gameRepository.findAll());
        return games;
    }

    public Game getGame(Integer id) {
        return gameRepository.findById(id).orElseThrow();
    }

    public void addGame(Game game) {
        gameRepository.save(game);
    }

    public void updateGame(Game game) {
        gameRepository.save(game);
    }

    public void delGame(Integer id) {
        gameRepository.deleteById(id);
    }
}
