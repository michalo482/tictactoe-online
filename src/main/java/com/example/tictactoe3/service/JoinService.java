package com.example.tictactoe3.service;

import com.example.tictactoe3.model.Join;
import com.example.tictactoe3.repository.JoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final JoinRepository joinRepository;

    public List<Join> getAllJoins() {
        List<Join> joined = new ArrayList<>();
        joinRepository.findAll().forEach(joined::add);
        return joined;
    }

    public Join getJoin(Long id) {
        return joinRepository.findById(id).orElseThrow();
    }

    public Join findByToken(String token) {
        return joinRepository.findByToken(token);
    }

    public void addJoin(Join join) {
        joinRepository.save(join);
    }

    public void deleteJoin(String token) {
        joinRepository.deleteByToken(token);
    }
}
