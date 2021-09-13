package com.example.tictactoe3.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(cascade = {CascadeType.ALL})
    private Board board;

    private boolean isThereSecondPlayer;
    private boolean isFirstPlayerTurn;
    private boolean isGameFinished;
    private GameStatus gameStatus;

    private static int i = 0;

    public Game() {
        Game.i++;
        this.board = new Board();
        this.gameStatus = GameStatus.IN_PROGRESS;
    }

    public static void setI(int i) {
        Game.i = i;
    }

    public static int getI() {
        return i;
    }

    public void setGameStatusBasedOnPlayer(boolean isFirstPlayer) {
        if(isFirstPlayer) {
            this.gameStatus = GameStatus.FIRST_PLAYER_WON;
        } else {
            this.gameStatus = GameStatus.SECOND_PLAYER_WON;
        }
    }
}
