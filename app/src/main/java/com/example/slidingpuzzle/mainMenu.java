package com.example.slidingpuzzle;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

public class mainMenu extends AppCompatActivity {

    static Integer size = 3;
    ImageView bmbLogo;
    Integer[] pics;
    Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        TextView logoText = findViewById(R.id.logoText);
        tf = Typeface.createFromAsset(getAssets(),  "fonts/comforta.ttf");
        logoText.setTypeface(tf);
        pics = new Integer[]{
                R.drawable.s3,
                R.drawable.s4,
                R.drawable.s5,
                R.drawable.s6,
                R.drawable.s7,
                R.drawable.s8,
                R.drawable.s9,
                R.drawable.s10
        };
        BoomMenuButton bmb = findViewById(R.id.bmb);
        bmbLogo = findViewById(R.id.bmbLogo);
        bmb.setButtonEnum(ButtonEnum.SimpleCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_8_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_8_1);
        for (int i = 0; i < bmb.getButtonPlaceEnum().buttonNumber(); i++) {
            final int finalI = i;
            bmb.addBuilder(new SimpleCircleButton.Builder()
                    .normalImageRes(pics[i])
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            bmbLogo.setImageResource(pics[finalI]);
                            size = finalI + 3;
                        }
                    }));
        }
        bmb.setDotRadius(0);
        bmb.setButtonRadius( (int) (bmb.getButtonRadius()*1.25));

        CustomBmb highScore = findViewById(R.id.highScore);
        highScore.setPage("score");
        highScore.setButtonRadius( (int) (highScore.getButtonRadius()*1.25));

        CustomBmb settings = findViewById(R.id.settings);
        settings.setPage("settings");
        settings.setButtonRadius( (int) (settings.getButtonRadius()*1.25));

        CustomBmb playBut = findViewById(R.id.playBut);
        playBut.setPage("play");
        playBut.setButtonRadius( (int) (playBut.getButtonRadius()*1.60));

    }


}
