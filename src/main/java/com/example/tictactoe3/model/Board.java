package com.example.tictactoe3.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.example.tictactoe3.model.BoardField.EMPTY;
import static com.example.tictactoe3.model.BoardField.X;
import static com.example.tictactoe3.model.BoardField.O;

@Entity
@Getter
@Setter
@Builder
public class Board {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ElementCollection
    private List<BoardField> board;

    private static final Logger logger = Logger.getLogger(Board.class.getName());
    private final static int board_size = 9;
    private final static String position_wrong = "Invalid position!";
    private final static String position_ok = "Valid position!";
    private final static int three_times = 3;

    public Board() {
        board = new ArrayList<>();
        for (int i = 0; i < board_size; i++) {
            board.add(EMPTY);
        }
    }

    public Board(Integer Id, List<BoardField> board) {
        super();
        this.id = Id;
        this.board = board;
    }

    public boolean setFieldIfAllowed(final boolean isFirstPlayerTurn, int x, final int y) {
        if(isFirstPlayerTurn) {
            return setSharpIfAllowed(x,y);
        } else {
            return setCircleIfAllowed(x,y);
        }
    }

    private boolean setSharpIfAllowed(final int x, final int y) {
        return setCharacterIfAllowed(x, y, X);
    }

    private boolean setCircleIfAllowed(final int x, final int y) {
        return setCharacterIfAllowed(x, y, O);
    }

    public boolean checkIsGameOver(boolean isFirstPlayer) {
        BoardField field = X;
        if(!isFirstPlayer) {
            field = O;
        }

        if (checkIsGameOverDiagonally(field)) {
            return true;
        }
        if (checkIsGameOverVertically(field)) {
            return true;
        }
        if (checkIsGameOverHorizontally(field)) {
            return true;
        }
        return false;
    }

    public boolean checkIsGameTie() {
        for(BoardField field : board){
            if(field.equals(EMPTY)) {
                return false;
            }
        }
        return true;
    }

    private boolean setCharacterIfAllowed(final int x, final int y, final BoardField field) {
        boolean isAllowed = false;
        int index = (x + y) - 1;
        if (board.get(index).equals(EMPTY)) {
            board.set(index, field);
            isAllowed = true;
            System.out.println(position_ok);
        } else {
            System.out.println(position_wrong + "Cannot insert in that position!");
        }
        printBoard();
        return isAllowed;
    }

    private void printBoard() {
        for (int i = 0; i < board_size; i++) {
            System.out.print(board.get(i) + " ");
            if ((i + 1) % 3 == 0)
                System.out.println("");
        }
        System.out.println("___________");
    }

    private boolean checkIsGameOverDiagonally(final BoardField field) {
        if ((board.get(0).equals(field) && board.get(4).equals(field) && board.get(8).equals(field))
                || (board.get(2).equals(field) && board.get(4).equals(field) && board.get(6).equals(field))) {
            return true;
        }
        return false;
    }

    private boolean checkIsGameOverVertically(final BoardField field) {

        int howManyTimes = 0;
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 6; j = j + 3) {
                if (board.get(i + j).equals(field)) {
                    howManyTimes++;
                    if (howManyTimes == three_times) {
                        return true;
                    }
                } else {
                    break;
                }
            }
            howManyTimes = 0;
        }
        return false;
    }

    private boolean checkIsGameOverHorizontally(final BoardField field) {

        int howManyTimes = 0;
        for (int i = 0; i <= 6; i = i + 3) {
            for (int j = 0; j <= 2; j++) {
                if (board.get(i + j).equals(field)) {
                    howManyTimes++;
                    if (howManyTimes == three_times) {
                        return true;
                    }
                } else break;
            }
            howManyTimes = 0;
        }
        return false;
    }
}
