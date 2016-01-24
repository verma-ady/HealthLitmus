package com.healthlitmus.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthlitmus.Helper.GradientOverImageDrawable;
import com.healthlitmus.R;

public class Splash extends Activity {


    TextView textViewHead1, textViewHead2;
    Animation animation;
    ImageView imageViewBG;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageViewBG = (ImageView) findViewById(R.id.background_splash);
        Bitmap bitmapBG = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        int startColor = Color.argb(175, 0, 0, 0);
        int endColor =Color.argb(175, 0, 0, 0);
        GradientOverImageDrawable gradientOverImageDrawableBG = new GradientOverImageDrawable(getResources(), bitmapBG);
        gradientOverImageDrawableBG.setGradientColors(startColor, endColor);
        imageViewBG.setImageDrawable(gradientOverImageDrawableBG);

        textViewHead1 = (TextView) findViewById(R.id.text_login_head1);
        Typeface narrow = Typeface.createFromAsset(getAssets(), "fonts/arial_narrow.ttf");
        textViewHead1.setTypeface(narrow, Typeface.BOLD);
        textViewHead2 = (TextView) findViewById(R.id.text_login_head2);
        textViewHead2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/arial_narrow.ttf"));
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        animation.setDuration(2000);
        linearLayout.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(Splash.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


//        Thread thread = new Thread() {
//            public void run() {
//                try {
//                    sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    Intent intent = new Intent(Splash.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        };
//        thread.start();

    }
}
