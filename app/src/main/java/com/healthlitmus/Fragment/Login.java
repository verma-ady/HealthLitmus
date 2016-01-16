package com.healthlitmus.Fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.healthlitmus.Helper.GradientOverImageDrawable;
import com.healthlitmus.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment implements Animation.AnimationListener{


    public Login() {
        // Required empty public constructor
    }

    ImageView imageViewBG;
    Button buttonLogInHealthLitmus;
    Animation animationButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FacebookSdk.sdkInitialize(getContext());
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        imageViewBG = (ImageView) view.findViewById(R.id.background_login);
        Bitmap bitmapBG = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        int startColor = Color.argb(175, 0, 0, 0);
        int endColor =Color.argb(175, 0, 0, 0);
        GradientOverImageDrawable gradientOverImageDrawableBG = new GradientOverImageDrawable(getResources(), bitmapBG);
        gradientOverImageDrawableBG.setGradientColors(startColor, endColor);
        imageViewBG.setImageDrawable(gradientOverImageDrawableBG);

        buttonLogInHealthLitmus = (Button) view.findViewById(R.id.button_login_hl);

        ButtonListener();
        return view;
    }

    private void ButtonListener(){

        buttonLogInHealthLitmus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationButton = AnimationUtils.loadAnimation(getContext(), R.anim.aplha);
                v.startAnimation(animationButton);
                animationButton.setAnimationListener(Login.this);
            }
        });
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
