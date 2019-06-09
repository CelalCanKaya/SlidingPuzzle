package com.example.myapplication;

public class GameBg {

    private int boardSize;
    private Piece boardArr[][];

    public GameBg(int boardSize) {
        this.boardSize = boardSize;
        boardArr = new Piece[boardSize][boardSize];
        for(int i=0; i<boardSize; i++){
            for(int j=0; j<boardSize; j++){
                boardArr[i][j] = new Piece(i,j);
            }
        }
    }


}
