package com.cckstudio.slidingpuzzle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import com.nightonke.boommenu.BoomMenuButton;

public class CustomBmb extends BoomMenuButton {

    String page;
    Context context;


    public CustomBmb(Context context) {
        super(context);
        this.context=context;
    }

    public CustomBmb(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public CustomBmb(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    public void setPage(String page) {
        this.page = page;
    }

    @Override
    public void boom() {
        changeActivity(page);
    }

    public void changeActivity(String page){
        switch (page) {
            case "score": {
                Intent intent = new Intent(context, HighScore.class);
                context.startActivity(intent);
                mainMenu.size = 3;
                ((Activity) context).finish();
                break;
            }
            case "settings": {
                Intent intent = new Intent(context, Settings.class);
                context.startActivity(intent);
                mainMenu.size = 3;
                ((Activity) context).finish();
                break;
            }
            case "play": {
                Intent intent = new Intent(context, GameScreen.class);
                intent.putExtra("size_string", mainMenu.size);
                mainMenu.size = 3;
                context.startActivity(intent);
                ((Activity) context).finish();
                break;
            }
        }
    }
}
