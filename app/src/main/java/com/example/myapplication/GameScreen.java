package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

public class GameScreen extends AppCompatActivity {


    GridLayout layout;
    GameBg game;
    MediaPlayer mp;
    Dialog dialog;
    TextView upperText, lowerText;
    Button again, menu;
    Typeface tf;
    Integer size = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        size = getIntent().getExtras().getInt("size_string");
        game = new GameBg(size);
        layout = findViewById(R.id.lin);
        dialog=new Dialog(this);
        tf = Typeface.createFromAsset(getAssets(),  "fonts/comforta.ttf");
        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameScreen.this, mainMenu.class);
                startActivity(intent);
                finish();
            }
        });
        drawBoard(layout,size);
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
                        Button btn = new Button(GameScreen.this);
                        setButtonProperties(width, height, i, boardSize, btn);
                        layout.addView(btn);
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
        but.setBackgroundResource(R.drawable.box);
        but.setTextSize(50f-boardSize*4);
        but.setTypeface(tf);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ret = game.move((Integer.parseInt(text)-1)/boardSize, (Integer.parseInt(text)-1)%boardSize);
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
                        clickSoundEffect(mp);
                        isGameFinished();
                        animationY(but,tempHeight);
                        break;
                    case "down":
                        clickSoundEffect(mp);
                        isGameFinished();
                        animationY(but,-tempHeight);
                        break;
                    case "left":
                        clickSoundEffect(mp);
                        isGameFinished();
                        animationX(but,-tempWidth);
                        break;
                    case "right":
                        clickSoundEffect(mp);
                        isGameFinished();
                        animationX(but,tempWidth);
                        break;
                    default:
                        System.out.println("Cant Move");
                }
                game.isGameFinished();
            }
        });
    }

    private void animationX(final Button but, int tempWidth){
        ObjectAnimator animation = ObjectAnimator.ofFloat(but, "translationX", but.getTranslationX()+tempWidth);
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                but.setClickable(true);
            }
            @Override
            public void onAnimationStart(Animator animation) {
                but.setClickable(false);
            }
        });
        animation.setDuration(125);
        animation.start();
    }

    private void animationY(final Button but, int tempHeight){
        ObjectAnimator animation = ObjectAnimator.ofFloat(but, "translationY", but.getTranslationY()-tempHeight);
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                but.setClickable(true);
            }
            @Override
            public void onAnimationStart(Animator animation) {
                but.setClickable(false);
            }
        });
        animation.setDuration(125);
        animation.start();
    }

    private void clickSoundEffect(MediaPlayer mp){
        if(mp.isPlaying()){
            mp.stop();
            mp.release();
            mp = MediaPlayer.create(GameScreen.this, R.raw.click);
        }
        mp.start();
    }

    private void isGameFinished(){
        if(game.isGameFinished()){
            dialog.setContentView(R.layout.end_game);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            again = dialog.findViewById(R.id.again);
            menu = dialog.findViewById(R.id.menu);
            upperText = dialog.findViewById(R.id.upperText);
            lowerText = dialog.findViewById(R.id.lowerText);
            again.setTypeface(tf);
            menu.setTypeface(tf);
            upperText.setTypeface(tf);
            lowerText.setTypeface(tf);
            again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    recreate();
                }
            });
        }
    }
}
