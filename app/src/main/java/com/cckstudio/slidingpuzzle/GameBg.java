package com.cckstudio.slidingpuzzle;

import java.util.ArrayList;
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

    public ArrayList<Integer> whichPiecesShouldMove(int x, int y){
        ArrayList<Integer> pieces = new ArrayList<>();
        int emptyPieceX = -1, emptyPieceY = -1, selectedPieceX = -1, selectedPieceY = -1, flag = 0;
        for(int i=0; i<boardSize; i++){
            for(int j=0; j<boardSize; j++){
                if(boardArr[i][j].getX()==x && boardArr[i][j].getY()==y){
                    selectedPieceX = i;
                    selectedPieceY = j;
                    flag++;
                }
                else if(boardArr[i][j].getX()==boardSize-1 && boardArr[i][j].getY()==boardSize-1){
                    emptyPieceX = i;
                    emptyPieceY = j;
                    flag++;
                }
                if(flag == 2){
                    break;
                }
            }
        }
        if(emptyPieceX == selectedPieceX){
            if(emptyPieceY>selectedPieceY){
                for(int i=emptyPieceY-1; i>=selectedPieceY; i--){
                    pieces.add(boardArr[selectedPieceX][i].getX()*boardSize+boardArr[selectedPieceX][i].getY());
                }
            }
            else{
                for(int i=emptyPieceY+1; i<=selectedPieceY; i++){
                    pieces.add(boardArr[selectedPieceX][i].getX()*boardSize+boardArr[selectedPieceX][i].getY());
                }
            }
        }
        else if(emptyPieceY == selectedPieceY){
            if(emptyPieceX>selectedPieceX){
                for(int i=emptyPieceX-1; i>=selectedPieceX; i--){
                    pieces.add(boardArr[i][selectedPieceY].getX()*boardSize+boardArr[i][selectedPieceY].getY());
                }
            }
            else{
                for(int i=emptyPieceX+1; i<=selectedPieceX; i++){
                    pieces.add(boardArr[i][selectedPieceY].getX()*boardSize+boardArr[i][selectedPieceY].getY());
                }
            }
        }
        return pieces;
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
