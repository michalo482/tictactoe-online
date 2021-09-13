package com.example.tictactoe3.utils;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TokenUtils {

    public String generateToken() {
        Random rand = new Random();
        return String.valueOf(rand.nextInt(1000));
    }
}
