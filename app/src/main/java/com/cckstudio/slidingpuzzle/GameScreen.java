package com.cckstudio.slidingpuzzle;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import com.cckstudio.slidingpuzzle.Enums.Enums;

import java.util.ArrayList;

public class GameScreen extends AppCompatActivity {

    GridLayout layout;
    GameBg game;
    MediaPlayer mp;
    Dialog dialog;
    Chronometer timer;
    TextView sizeText, moveCountText, highScoreText;
    Typeface tf;
    Integer size = 3, moveCount = 0;
    boolean isTimerStarted = false;
    private Button[] pieces;
    AnimatorSet animSet;
    ImageButton pauseButton;
    long timeWhenStopped = 0;
    boolean isVolumeOn;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        size = getIntent().getExtras().getInt("size_string");
        findViewIds();
        initializeObjects();
        setTypefaces();
        drawBoard(layout,size);
    }

    @Override
    public void onBackPressed() {
        showDialogScreen(Enums.DialogBoxes.PAUSE);
    }

    private void findViewIds(){
        sizeText = findViewById(R.id.sizeText);
        highScoreText = findViewById(R.id.highScoreText);
        layout = findViewById(R.id.lin);
        timer = findViewById(R.id.timer);
        moveCountText = findViewById(R.id.moveCount);
        pauseButton = findViewById(R.id.pauseButton);
    }

    private void initializeObjects(){
        game = new GameBg(size);
        sizeText.setText(size + " x " + size);
        dialog=new Dialog(this);
        pieces=new Button[size*size];
        tf = Typeface.createFromAsset(getAssets(),  "fonts/comforta.ttf");
        animSet = new AnimatorSet();
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isVolumeOn = sharedPreferences.getBoolean("isVolumeOn", true);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogScreen(Enums.DialogBoxes.PAUSE);
            }
        });
    }

    private void setTypefaces(){
        moveCountText.setTypeface(tf);
        sizeText.setTypeface(tf);
        timer.setTypeface(tf);
        highScoreText.setTypeface(tf);
    }

    public void showDialogScreen(Enums.DialogBoxes dialogType){
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if(dialogType == Enums.DialogBoxes.PAUSE){ // pause
            timeWhenStopped = timer.getBase() - SystemClock.elapsedRealtime();
            timer.stop();
            dialog.setContentView(R.layout.pause_game);
            ImageButton resumeButton = dialog.findViewById(R.id.resumeButton);
            ImageButton mainMenuButton = dialog.findViewById(R.id.mainMenuButton);
            final ImageButton soundButton = dialog.findViewById(R.id.soundButton);
            TextView pauseText = dialog.findViewById(R.id.pauseText);
            TextView resumeText = dialog.findViewById(R.id.resumeText);
            TextView mainMenuText = dialog.findViewById(R.id.mainMenuText);
            final TextView soundText = dialog.findViewById(R.id.soundText);
            pauseText.setTypeface(tf);
            resumeText.setTypeface(tf);
            mainMenuText.setTypeface(tf);
            soundText.setTypeface(tf);
            if(!isVolumeOn){
                soundButton.setBackgroundResource(R.drawable.ic_volume_off_pink);
            }
            resumeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if(isTimerStarted){
                        timer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                        timer.start();
                    }
                }
            });
            mainMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Intent intent = new Intent(GameScreen.this, MainMenu.class);
                    startActivity(intent);
                    finish();
                }
            });
            soundButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isVolumeOn){
                        soundButton.setBackgroundResource(R.drawable.ic_volume_up_pink);
                        soundText.setText("Turn Off Sound Effects");
                        editor.putBoolean("isVolumeOn", true);
                        editor.apply();
                        isVolumeOn = sharedPreferences.getBoolean("isVolumeOn", false);
                    }
                    else{
                        soundButton.setBackgroundResource(R.drawable.ic_volume_off_pink);
                        soundText.setText("Turn On Sound Effects");
                        editor.putBoolean("isVolumeOn", false);
                        editor.apply();
                        isVolumeOn = sharedPreferences.getBoolean("isVolumeOn", false);
                    }
                }
            });
            dialog.show();
        }
        else if(dialogType == Enums.DialogBoxes.GAME_FINISH){    //game finish
            dialog.setContentView(R.layout.end_game);
            Button again = dialog.findViewById(R.id.again);
            Button menu = dialog.findViewById(R.id.menu);
            TextView upperText = dialog.findViewById(R.id.upperText);
            TextView lowerText = dialog.findViewById(R.id.lowerText);
            again.setTypeface(tf);
            menu.setTypeface(tf);
            upperText.setTypeface(tf);
            lowerText.setTypeface(tf);
            long elapsedTime = SystemClock.elapsedRealtime() - timer.getBase();
            elapsedTime = elapsedTime / 1000;
            lowerText.setText("You Did It In " + elapsedTime / 60 + " Minutes " + elapsedTime % 60 + " Seconds! You moved " + moveCount + " Pieces!" );
            again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    recreate();
                }
            });
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(GameScreen.this, MainMenu.class);
                    startActivity(intent);
                    finish();
                }
            });
            dialog.show();
        }
    }

    private void drawBoard(final GridLayout layout, final int boardSize){
        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = layout.getMeasuredHeight();
                int width = layout.getMeasuredWidth();
                if(height>0 && width>0){
                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    for(int i=0; i<boardSize*boardSize-1; i++){
                        int xInd = game.getBoardArr()[i/boardSize][i%boardSize].getX();
                        int yInd = game.getBoardArr()[i/boardSize][i%boardSize].getY();
                        Button btn = new Button(GameScreen.this);
                        setButtonProperties(width, height, i, boardSize, btn);
                        int index = xInd*boardSize+yInd;
                        pieces[index] = btn;
                        layout.addView(pieces[index]);
                    }
                }
            }
        });
        layout.setRowCount(boardSize);
        layout.setColumnCount(boardSize);
    }

    private void setButtonProperties(int width, int height, int i, final int boardSize, final Button but){
        GridLayout.LayoutParams param =new GridLayout.LayoutParams();
        final int tempWidth = width/boardSize;
        final int tempHeight = height/boardSize;
        param.width = tempWidth;
        param.height = tempHeight;
        final String text = Integer.toString(1+game.getBoardArr()[i/boardSize][i%boardSize].getX()*boardSize+game.getBoardArr()[i/boardSize][i%boardSize].getY());
        but.setText(text);
        but.setLayoutParams (param);
        int color = Integer.parseInt(text) % 3;
        if(color == 0){
            but.setBackgroundResource(R.drawable.ic_coloredbox1);
        }
        else if(color == 1){
            but.setBackgroundResource(R.drawable.ic_coloredbox2);
        }
        else if(color == 2){
            but.setBackgroundResource(R.drawable.ic_coloredbox3);
        }
        but.setTextSize(50f-boardSize*4);
        but.setTypeface(tf);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ret = "";
                ArrayList<Integer> piecesWhichShouldMove = game.whichPiecesShouldMove((Integer.parseInt(text)-1)/boardSize, (Integer.parseInt(text)-1)%boardSize);
                for(int i=0; i<piecesWhichShouldMove.size(); i++){
                    ret = game.move((piecesWhichShouldMove.get(i))/boardSize, (piecesWhichShouldMove.get(i))%boardSize);
                }
                mp = MediaPlayer.create(GameScreen.this, R.raw.click);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                switch(ret)
                {
                    case "up":
                        startTimer();
                        clickSoundEffect(mp);
                        increaseMoveCounter();
                        isGameFinished();
                        animationY(piecesWhichShouldMove,tempHeight);
                        break;
                    case "down":
                        startTimer();
                        clickSoundEffect(mp);
                        increaseMoveCounter();
                        isGameFinished();
                        animationY(piecesWhichShouldMove,-tempHeight);
                        break;
                    case "left":
                        startTimer();
                        clickSoundEffect(mp);
                        increaseMoveCounter();
                        isGameFinished();
                        animationX(piecesWhichShouldMove,-tempWidth);
                        break;
                    case "right":
                        startTimer();
                        clickSoundEffect(mp);
                        increaseMoveCounter();
                        isGameFinished();
                        animationX(piecesWhichShouldMove, tempWidth);
                        break;
                    default:
                        System.out.println("Cant Move");
                }
                game.isGameFinished();
            }
        });
    }

    private void increaseMoveCounter(){
        moveCount++;
        moveCountText.setText(moveCount + "");
    }

    private void animationX(final ArrayList<Integer> piecesWhichShouldMove, int tempWidth){
        if(animSet.isRunning()){
            animSet.end();
        }
        animSet = new AnimatorSet();
        final Animator anims[] = new Animator[piecesWhichShouldMove.size()];
        for(int i=0; i<piecesWhichShouldMove.size(); i++){
            anims[i] = ObjectAnimator.ofFloat(pieces[piecesWhichShouldMove.get(i)], "translationX", pieces[piecesWhichShouldMove.get(i)].getTranslationX()+tempWidth);
        }
        animSet.playTogether(anims);
        animSet.setDuration(125);
        animSet.start();
    }

    private void animationY(final ArrayList<Integer> piecesWhichShouldMove, int tempHeight){
        if(animSet.isRunning()){
            animSet.end();
        }
        animSet = new AnimatorSet();
        final Animator anims[] = new Animator[piecesWhichShouldMove.size()];
        for(int i=0; i<piecesWhichShouldMove.size(); i++){
            anims[i] = ObjectAnimator.ofFloat(pieces[piecesWhichShouldMove.get(i)], "translationY", pieces[piecesWhichShouldMove.get(i)].getTranslationY()-tempHeight);
        }
        animSet.playTogether(anims);
        animSet.setDuration(125);
        animSet.start();
    }

    private void clickSoundEffect(MediaPlayer mp){
        if(isVolumeOn){
            if(mp.isPlaying()){
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(GameScreen.this, R.raw.click);
            }
            mp.start();
        }
    }

    private void isGameFinished(){
        if(game.isGameFinished()){
            timer.stop();
            showDialogScreen(Enums.DialogBoxes.GAME_FINISH);
        }
    }

    private void startTimer(){
        if(!isTimerStarted){
            isTimerStarted=true;
            timer.setBase(SystemClock.elapsedRealtime());
            timer.start();
        }
    }
}
