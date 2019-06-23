package com.example.myapplication;

import java.util.Arrays;
import java.util.Collections;

public class GameBg {

    private int boardSize;
    private Piece boardArr[][];

    public Piece[][] getBoardArr() {
        return boardArr;
    }

    public GameBg(int boardSize) {
        this.boardSize = boardSize;
        boardArr = new Piece[boardSize][boardSize];
        initGame();
    }

    private void initGame(){
        int tempBoardSize = boardSize*boardSize;
        Piece tempBoardArr[] = new Piece[tempBoardSize];
        for(int i=0; i<tempBoardSize; i++){
            tempBoardArr[i] = new Piece(i/boardSize,i%boardSize);
        }
        do{
            Collections.shuffle(Arrays.asList(tempBoardArr).subList(0,tempBoardSize-1));
        }
        while(isSorted(tempBoardArr));
        for(int i=0; i<boardSize; i++){
            for(int j=0; j<boardSize; j++){
                boardArr[i][j] = tempBoardArr[i*boardSize+j];
            }
        }
    }

    public boolean isGameFinished(){
        for(int i=0; i<boardSize; i++){
            for(int j=0; j<boardSize; j++){
                if(boardArr[i][j].getX()!=i || boardArr[i][j].getY()!=j){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSorted(Piece[] arr){
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i].getX() >= arr[i + 1].getX() && arr[i].getY() >= arr[i + 1].getY()) {
                return false;
            }
        }
        return true;
    }

    public String move(int x, int y){
        for(int i=0; i<boardSize; i++){
            for(int j=0; j<boardSize; j++){
                if(boardArr[i][j].getX()==x && boardArr[i][j].getY()==y){
                    if(i-1>=0 && boardArr[i-1][j].getX()==boardSize-1 && boardArr[i-1][j].getY()==boardSize-1){
                        swap(i,j,i-1,j);
                        return "up";
                    }
                    else if(i+1<boardSize && boardArr[i+1][j].getX()==boardSize-1 && boardArr[i+1][j].getY()==boardSize-1){
                        swap(i,j,i+1,j);
                        return "down";
                    }
                    else if(j-1>=0 && boardArr[i][j-1].getX()==boardSize-1 && boardArr[i][j-1].getY()==boardSize-1){
                        swap(i,j,i,j-1);
                        return "left";
                    }
                    else if(j+1<boardSize && boardArr[i][j+1].getX()==boardSize-1 && boardArr[i][j+1].getY()==boardSize-1){
                        swap(i,j,i,j+1);
                        return "right";
                    }
                }
            }
        }
        return "cantMove";
    }

    private void swap(int x1, int y1, int x2, int y2){
        Piece tempPiece = boardArr[x1][y1];
        boardArr[x1][y1]=boardArr[x2][y2];
        boardArr[x2][y2]=tempPiece;
    }

}
