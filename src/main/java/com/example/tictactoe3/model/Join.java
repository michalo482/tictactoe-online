package com.example.tictactoe3.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Join {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String token;

    @ManyToOne
    private Game game;

    private Boolean isFirstPlayer;

    public Boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public void setFirstPlayer(Boolean firstPlayer) {
        this.isFirstPlayer = firstPlayer;
    }
}
