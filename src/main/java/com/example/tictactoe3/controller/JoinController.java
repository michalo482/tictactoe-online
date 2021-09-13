package com.example.tictactoe3.controller;

import com.example.tictactoe3.model.Game;
import com.example.tictactoe3.model.Player;
import com.example.tictactoe3.service.GameService;
import com.example.tictactoe3.service.JoinService;
import com.example.tictactoe3.utils.Const;
import com.example.tictactoe3.utils.TokenUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JoinController {

    private JoinService joinService;
    private TokenUtils tokenUtils;
    private GameService gameService;

    @GetMapping("/players")
    public List<Player> getAllPlayers() {
        return joinService.getAllJoins();
    }

    @PostMapping("/game/{id}/join")
    public ResponseEntity<String> joinGame(@PathVariable Integer id) {
        Game game = gameService.getGame(id);
        if (null != game && (!game.isThereSecondPlayer())) {
            Player secondPlayer = createSecondPlayer(game);
            joinService.addJoin(secondPlayer);

            game.setThereSecondPlayer(true);
            game.setFirstPlayerTurn(true);
            gameService.addGame(game);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set(Const.SET_AUTH_TOKEN, secondPlayer.getToken());

            String body = "{ \"invitationUrl\": \"127.0.0.1:8087/game/{" + game.getId().toString() + "}/join\"}";
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(body);
        } else {
            return ResponseEntity.badRequest().body("Cannot join game!");
        }
    }

    private Player createSecondPlayer(Game game) {
        Player j = new Player();
        j.setGame(game);
        j.setToken(tokenUtils.generateToken());
        j.setFirstPlayer(false);
        return j;
    }
}
