package com.example.tictactoe3.controller;

import com.example.tictactoe3.model.Game;
import com.example.tictactoe3.model.GameStatus;
import com.example.tictactoe3.model.Player;
import com.example.tictactoe3.service.GameService;
import com.example.tictactoe3.service.JoinService;
import com.example.tictactoe3.utils.Const;
import com.example.tictactoe3.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final TokenUtils tokenUtils;
    private final JoinService joinService;

    @PostMapping("/game")
    public ResponseEntity<String> createGame(HttpServletRequest request) {
        Game game = new Game();
        game.setGameStatus(GameStatus.IN_PROGRESS);
        gameService.addGame(game);

        Player firstPlayer = createJoinForFirstPlayer(game);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(Const.SET_AUTH_TOKEN, firstPlayer.getToken());

        String baseUrl = String.format("{\"invitationUrl\": \"%s:%d%s/{%s}/join\"}",
                request.getServerName(), request.getServerPort(), "/game", game.getId().toString());

        return ResponseEntity.ok().headers(responseHeaders).body(baseUrl);
    }

    @PutMapping("/game/{gameId}")
    public void markField(@PathVariable Integer gameId,
                          @RequestBody Map<String, String> reqBody,
                          @RequestHeader HttpHeaders headers) {
        Game game = gameService.getGame(gameId);
        List<String> listHeaders = headers.get(Const.SET_AUTH_TOKEN);

        if (null != game && listHeaders != null && !listHeaders.isEmpty() && !game.isGameFinished()) {
            String authToken = listHeaders.get(0);
            if (isTokenCorrectForGame(authToken, gameId)) {
                if (isBodyCorrect(reqBody)) {
                    final String x = reqBody.get(Const.POSITION).substring(0, 1);
                    final String y = reqBody.get(Const.POSITION).substring(1, 2);

                    Player player = joinService.findByToken(authToken);
                    if ((player.getIsFirstPlayer() && game.isFirstPlayerTurn())
                            || (!player.getIsFirstPlayer() && !game.isFirstPlayerTurn())) {
                        boolean isMovementCorrect = game.getBoard().setFieldIfAllowed(game.isFirstPlayerTurn(),
                                calculateX(x), Integer.valueOf(y));
                        if (isMovementCorrect) {
                            if (game.getBoard().checkIsGameTie()) {
                                game.setGameFinished(true);
                                game.setGameStatus(GameStatus.TIE);
                                System.out.println("TIE!");
                            } else if (game.getBoard().checkIsGameOver(game.isFirstPlayerTurn())) {
                                game.setGameFinished(true);
                                game.setGameStatusBasedOnPlayer(game.isFirstPlayerTurn());
                                System.out.println("You won!");
                            }
                            game.setFirstPlayerTurn(!game.isFirstPlayerTurn());
                        } else {
                            System.out.println("Movement incorrect try again");
                        }
                    } else {
                        System.out.println("Its not ur turn, wait!");
                    }
                    gameService.updateGame(game);
                }
            } else {
                throw new IllegalArgumentException("Authorization problem!");
            }
        }
    }

    @GetMapping("/game/{gameId}")
    public @ResponseBody ResponseEntity<String> gameStatus(@PathVariable Integer gameId,
                                                           @RequestHeader HttpHeaders headers) {
        Game game = gameService.getGame(gameId);
        List<String> listHeaders = headers.get(Const.SET_AUTH_TOKEN);

        if (null != game && listHeaders != null && !listHeaders.isEmpty()) {
            List<Player> allPlayers = joinService.getAllJoins();
            String authToken = listHeaders.get(0);

            Long countOfPlayers = allPlayers.stream()
                    .filter(player -> player.getGame().getId() == gameId)
                    .count();
            if (countOfPlayers > 1 && isTokenCorrectForGame(authToken, gameId)) {
                Player player = joinService.findByToken(authToken);
                if (game.isGameFinished()) {
                    if (player.isFirstPlayer()) {
                        if (game.getGameStatus().equals(GameStatus.FIRST_PLAYER_WON)) {
                            return ResponseEntity.ok().body(Const.YOU_WON);
                        } else if (game.getGameStatus().equals(GameStatus.SECOND_PLAYER_WON)) {
                            return ResponseEntity.ok().body(Const.YOU_LOST);
                        } else {
                            return ResponseEntity.ok().body(Const.TIE);
                        }
                    } else {
                        if (game.getGameStatus().equals(GameStatus.FIRST_PLAYER_WON)) {
                            return ResponseEntity.ok().body(Const.YOU_LOST);
                        } else if (game.getGameStatus().equals(GameStatus.SECOND_PLAYER_WON)) {
                            return ResponseEntity.ok().body(Const.YOU_WON);
                        } else {
                            return ResponseEntity.ok().body(Const.TIE);
                        }
                    }
                } else {
                    if ((player.isFirstPlayer() && game.isFirstPlayerTurn()) || (!player.isFirstPlayer() && !game.isFirstPlayerTurn())) {
                        return ResponseEntity.ok().body(Const.YOUR_TURN);
                    } else return ResponseEntity.ok().body(Const.OPPONENT_TURN);
                }
            } else {
                return ResponseEntity.ok().body(Const.AWAITING_FOR_PLAYER);
            }
        }
        throw new IllegalArgumentException("cannot find game");
    }

    private boolean throwExcIfPositionsIncorrect(final String x, final String y) {
        if (x == null) {
            throw new IllegalArgumentException("First dimension should not be empty, choose A or B or C");
        } else if (!(x.equalsIgnoreCase("A") || x.equalsIgnoreCase("B") || x.equalsIgnoreCase("C"))) {
            throw new IllegalArgumentException("First dimension wrong, choose A or B or C");
        }
        if (y == null) {
            throw new IllegalArgumentException("Second dimension should not be empty, choose 1 or 2 or 3");
        } else if (Integer.valueOf(y) < 1 || Integer.valueOf(y) > 3) {
            throw new IllegalArgumentException("Second dimension wrong, choose 1 or 2 or 3");
        }
        return true;
    }

    public int calculateX(final String x) {
        int result = 7;
        if (x.equalsIgnoreCase("A"))
            result = 0;
        if (x.equalsIgnoreCase("B"))
            result = 3;
        if (x.equalsIgnoreCase("C"))
            result = 6;

        return result;
    }

    private boolean isBodyCorrect(Map<String, String> body) {
        final String jsonString = body.get(Const.POSITION);
        boolean isBodyCorrect;
        if (jsonString != null) {
            final String x = jsonString.substring(0, 1);
            final String y = jsonString.substring(1, 2);
            isBodyCorrect = throwExcIfPositionsIncorrect(x, y);
        } else {
            throw new IllegalArgumentException("Cannot find position parameter in request body!");
        }
        return isBodyCorrect;
    }

    private boolean isTokenCorrectForGame(String authToken, Integer gameId) {
        Player j = joinService.findByToken(authToken);
        if (j != null && gameId != null && j.getGame().getId().equals(gameId)) {
            return true;
        } else
            return false;
    }

    @GetMapping("/games")
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }

    private Player createJoinForFirstPlayer(Game game) {
        Player firstJoiner = new Player();
        firstJoiner.setGame(game);
        firstJoiner.setToken(tokenUtils.generateToken());
        firstJoiner.setFirstPlayer(true);
        joinService.addJoin(firstJoiner);
        return firstJoiner;
    }
}
