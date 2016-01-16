package com.healthlitmus.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.healthlitmus.Helper.ConnectionDetector;
import com.healthlitmus.Helper.GradientOverImageDrawable;
import com.healthlitmus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class Login extends AppCompatActivity {

    ImageView imageViewBG;
    Button buttonLogInHealthLitmus;
    Animation animationButtonAlpha, animationTextViewFade;
    TextView textViewNewUser;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LoginButton loginButton;
    Toolbar toolbar;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);

        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", getApplicationContext().MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setContentView(R.layout.activity_login);

        imageViewBG = (ImageView) findViewById(R.id.background_login);
        Bitmap bitmapBG = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        int startColor = Color.argb(175, 0, 0, 0);
        int endColor =Color.argb(175, 0, 0, 0);
        GradientOverImageDrawable gradientOverImageDrawableBG = new GradientOverImageDrawable(getResources(), bitmapBG);
        gradientOverImageDrawableBG.setGradientColors(startColor, endColor);
        imageViewBG.setImageDrawable(gradientOverImageDrawableBG);

        buttonLogInHealthLitmus = (Button) findViewById(R.id.button_login_hl);

        toolbar = (Toolbar) findViewById(R.id.toolbar_Login);
        // enabling toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        Drawable dr = getResources().getDrawable(R.drawable.health);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 40, 40, true));
        getSupportActionBar().setLogo(d);

        textViewNewUser = (TextView) findViewById(R.id.text_login_newuser);

        Log.v("MyApp", getClass().toString() + " Not Logged In");
        updateWithToken(AccessToken.getCurrentAccessToken());
        loginButton = (LoginButton) findViewById(R.id.buttonRegisterFacebook);
        ButtonListener();
        ViewListener();
    }

    private void ViewListener(){
        textViewNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationTextViewFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                animationTextViewFade.setDuration(250);
                v.startAnimation(animationTextViewFade);
                animationTextViewFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                animationTextViewFade.setDuration(250);
                v.startAnimation(animationTextViewFade);
                animationTextViewFade.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(Login.this, MyHealthLitmus.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

    private void ButtonListener(){
       buttonLogInHealthLitmus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationButtonAlpha = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.aplha);
                v.startAnimation(animationButtonAlpha);
                animationButtonAlpha.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //code to login user
                        Toast.makeText(getApplicationContext(), " Will Login User ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (false) {
                    Toast.makeText(getApplicationContext(),"False",Toast.LENGTH_LONG).show();
                    return;
                }
                FBLogin();
            }
        });
    }

    private void FBLogin(){
        ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());
        if( connectionDetector.isConnectingToInternet() ) {

            accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                    updateWithToken(newAccessToken);
                }
            };

            updateWithToken(AccessToken.getCurrentAccessToken());

            callbackManager = CallbackManager.Factory.create();

            loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
            loginButton.registerCallback(callbackManager, callback);
        } else {
            Toast.makeText(getApplicationContext(), "Connect To Internet To Validate Authentication ", Toast.LENGTH_LONG).show();
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Connect To Internet To Validate Authentication ",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void updateWithToken( AccessToken currentAccessToken ){
        if(currentAccessToken!=null){
            Log.v("MyApp", getClass().toString() + "Login updateWithToken not null ");

            GraphRequest request = GraphRequest.newMeRequest(currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.v("MyApp",getClass().toString() + response.toString());
                    // Get facebook data from login
//                    Bundle bFacebookData = getFacebookData(object);
                    try {
                        Log.v("MyApp", getClass().toString() +object.toString() );
                        Log.v("MyApp", getClass().toString() + object.getString("email"));

                        editor.putString("fname",object.getString("first_name"));
                        editor.putString("lname",object.getString("last_name"));
                        editor.putString("email", object.getString("email"));
                        editor.putString("gender", CapitalizeWord(object.getString("gender")));
                        editor.commit();

                        Intent intent = new Intent(Login.this, MyHealthLitmus.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        finish();

                    } catch (JSONException e) {
                        Log.v("MyApp", getClass().toString() + "LoginJSON");
//                        Toast.makeText(getApplicationContext(), "Unable to get your EMail-ID", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });

            Bundle parameters = new Bundle();
            // Parámetros que pedimos a facebook
            parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
            request.setParameters(parameters);
            request.executeAsync();

        } else {
            Log.v("MyApp", getClass().toString() + "Login updateWithToken null ");
//            dialog.dismiss();
        }
    }//updatewithtoken

    private String CapitalizeWord ( String s) {
        StringBuilder stringBuilder = new StringBuilder(s);
        stringBuilder.setCharAt(0, Character.toUpperCase(s.charAt(0)));
        return stringBuilder.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
