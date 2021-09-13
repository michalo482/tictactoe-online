package com.example.tictactoe3.service;

import com.example.tictactoe3.model.Player;
import com.example.tictactoe3.repository.JoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final JoinRepository joinRepository;

    public List<Player> getAllJoins() {
        List<Player> joined = new ArrayList<>();
        joinRepository.findAll().forEach(joined::add);
        return joined;
    }

    public Player getJoin(Long id) {
        return joinRepository.findById(id).orElseThrow();
    }

    public Player findByToken(String token) {
        return joinRepository.findByToken(token);
    }

    public void addJoin(Player player) {
        joinRepository.save(player);
    }

    public void deleteJoin(String token) {
        joinRepository.deleteByToken(token);
    }
}
