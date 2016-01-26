package com.healthlitmus.Activity;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
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

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;

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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.healthlitmus.Helper.ConnectionDetector;
import com.healthlitmus.Helper.GradientOverImageDrawable;
import com.healthlitmus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;

    ImageView imageViewBG;
    Button buttonLogInHealthLitmus, buttonLoginGooglePlus, buttonLogInFB;
    Animation animationButtonAlpha, animationTextViewFade, animationButtonScale;
    TextView textViewNewUser, textViewHead1, textViewHead2;
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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // enabling toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        Drawable dr = ContextCompat.getDrawable(getApplicationContext(),R.drawable.health);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 40, 40, true));
        getSupportActionBar().setLogo(d);

        buttonLogInHealthLitmus = (Button) findViewById(R.id.button_login_hl);
        buttonLoginGooglePlus = (Button) findViewById(R.id.button_login_gplus);
        buttonLogInFB = (Button) findViewById(R.id.button_login_fb);
//        loginButton = (LoginButton) findViewById(R.id.buttonRegisterFacebook);
        textViewNewUser = (TextView) findViewById(R.id.text_login_newuser);

        ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());
        if( !connectionDetector.isConnectingToInternet() ) {
            Log.v("MyApp", getClass().toString() + " No Internet Connection");
            Toast.makeText(getApplicationContext(), "Connect To Internet To Validate Authentication", Toast.LENGTH_LONG).show();
            buttonLoginGooglePlus.setClickable(false);
            buttonLogInHealthLitmus.setClickable(false);
            buttonLogInFB.setClickable(false);
//            loginButton.setClickable(false);
            textViewNewUser.setClickable(false);
        } else {
            buttonLoginGooglePlus.setClickable(true);
            buttonLogInHealthLitmus.setClickable(true);
            buttonLogInFB.setClickable(true);
//            loginButton.setClickable(true);
            textViewNewUser.setClickable(true);
        }

        textViewHead1 = (TextView) findViewById(R.id.text_login_head1);
        Typeface narrow = Typeface.createFromAsset(getAssets(), "fonts/arial_narrow.ttf");
        textViewHead1.setTypeface(narrow, Typeface.BOLD);
        textViewHead2 = (TextView) findViewById(R.id.text_login_head2);
        textViewHead2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/arial_narrow.ttf"));

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.google_login);
        drawable.setBounds(0, 0, 80, 80);
        buttonLoginGooglePlus.setCompoundDrawables(drawable, null, null, null);

        drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.fb_login);
        drawable.setBounds(0, 0, 64, 64);
        buttonLogInFB.setCompoundDrawables(drawable, null, null, null);

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN)).requestEmail().build();
//        Log.v("MyApp", "onCreate() 1");
        //Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).addApi(Plus.API).build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this )
//                .addApi(Plus.API)
//                .addScope(Scopes.PLUS_LOGIN)
//                .addScope(Scopes.PLUS_ME)
//                .build();

        ButtonListener();
        ViewListener();
        Ripple();
    }

    private void Ripple(){
        MaterialRippleLayout.on(buttonLogInHealthLitmus).rippleColor(Color.BLACK).create();
    }

    private void ViewListener(){
        textViewNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationTextViewFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                animationTextViewFade.setDuration(250);
                v.startAnimation(animationTextViewFade);
                animationTextViewFade.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        editor.putString("loginVia", "hlNew");
                        editor.commit();
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
                editor.putString("loginVia", "hl");
                editor.commit();
                animationButtonAlpha = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.aplha);
                v.startAnimation(animationButtonAlpha);
                animationButtonAlpha.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation){
                        //code to login user
                        Intent intent = new Intent(Login.this, AlreadyUserLogin.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }
        });

        buttonLogInFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("loginVia", "fb");
                editor.commit();
                animationButtonScale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
                v.startAnimation(animationButtonScale);
                animationButtonScale.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.fb_pressed);
                        drawable.setBounds(0, 0, 64, 64);
                        buttonLogInFB.setCompoundDrawables(drawable, null, null, null);
                        buttonLogInFB.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.facebookPressed));
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.fb_login);
                        drawable.setBounds(0, 0, 64, 64);
                        buttonLogInFB.setCompoundDrawables(drawable, null, null, null);
                        buttonLogInFB.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.com_facebook_blue));
                        loginButton = new LoginButton(Login.this);
                        loginButton.performClick();
                        accessTokenTracker = new AccessTokenTracker() {
                            @Override
                            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                                updateWithToken(newAccessToken);
                            }
                        };

                        callbackManager = CallbackManager.Factory.create();

                        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
                        loginButton.registerCallback(callbackManager, callback);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        buttonLoginGooglePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationButtonScale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
                v.startAnimation(animationButtonScale);
                animationButtonScale.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.google_pressed);
                        drawable.setBounds(0, 0, 64, 64);
                        buttonLoginGooglePlus.setCompoundDrawables(drawable, null, null, null);
                        buttonLoginGooglePlus.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.googlePressed));
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.google_login);
                        drawable.setBounds(0, 0, 64, 64);
                        buttonLoginGooglePlus.setCompoundDrawables(drawable, null, null, null);
                        buttonLoginGooglePlus.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.google_login));
                        signIn();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

    // Google Sign In ---------------------------------------------------------------------------------------
    private void signIn() {
//        Log.v("MyApp", "signIn Login");
        editor.putString("loginVia", "google");
        editor.commit();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.v("MyApp", getClass().toString() + " handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            Log.v("MyApp", getClass().toString() + " Google handleSignInResult() if Login");
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if(mGoogleApiClient.hasConnectedApi(Plus.API)){
                Person person  = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                Log.v("MyApp", "--------------------------------");
                String a[] = person.getDisplayName().split(" ");

                try {
                    int sz = a.length;
                    if(sz>2) {
                        editor.putString("fname", a[0] + " " + a[1]);
                        editor.putString("lname", a[2]);
                    } else {
                        editor.putString("fname", a[0]);
                        editor.putString("lname", a[1]);
                    }
                    editor.putString("gender", person.getGender() == 0 ? "Male" : "Female");
                    editor.putString("email", acct.getEmail());
                } catch (NullPointerException e ){
                    Log.v("MyApp", getClass().toString() + " NullPointerException" );
                }
            } else {
                String a[] = acct.getDisplayName().split(" ");
                try {
                    int sz = a.length;
                    if(sz>2) {
                        editor.putString("fname", a[0] + " " + a[1]);
                        editor.putString("lname", a[2]);
                    } else {
                        editor.putString("fname", a[0]);
                        editor.putString("lname", a[1]);
                    }
                    editor.putString("email", acct.getEmail());
                } catch (NullPointerException e ){
                    Log.v("MyApp", getClass().toString() + " NullPointerException" );
                }
            }
            editor.commit();
//            Toast.makeText(this, "GOT IT", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MyHealthLitmus.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            } else {
                // Signed out, show unauthenticated UI.
                Log.v("MyApp", getClass().toString() + "Google handleSignInResult() else Login");
                Toast.makeText(this, "Unable Sign In", Toast.LENGTH_SHORT).show();
            }
    }
    //--------------------------------------------------------------------------------------- Google Sign In

    // FaceBook Sign In ---------------------------------------------------------------------------------------
    private void updateWithToken( AccessToken currentAccessToken ){
        if(currentAccessToken!=null){
            Log.v("MyApp", getClass().toString() + "updateWithToken:If(Token NonNull)");

            GraphRequest request = GraphRequest.newMeRequest(currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.v("MyApp",getClass().toString() + response.toString());
                    // Get facebook data from login
//                    Bundle bFacebookData = getFacebookData(object);
                    try {
                        Log.v("MyApp", getClass().toString() + object.toString());

                        editor.putString("fname", object.getString("first_name"));
                        editor.putString("lname", object.getString("last_name"));
                        editor.putString("gender", CapitalizeWord(object.getString("gender")));
                        editor.putString("email", object.getString("email"));

                    } catch (JSONException e) {
                        Log.v("MyApp", getClass().toString() + "LoginJSON");
//                        Toast.makeText(getApplicationContext(), "Unable to get your EMail-ID", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    editor.commit();
                    Intent intent = new Intent(Login.this, MyHealthLitmus.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });

            Bundle parameters = new Bundle();
            // Parámetros que pedimos a facebook
            parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
            request.setParameters(parameters);
            request.executeAsync();

        } else {
            Log.v("MyApp", getClass().toString() + "updateWithToken:Else(Token Null)");
//            dialog.dismiss();
        }
    }//updatewithtoken
    //--------------------------------------------------------------------------------------- FaceBook Sign In

    private String CapitalizeWord ( String s) {
        StringBuilder stringBuilder = new StringBuilder(s);
        stringBuilder.setCharAt(0, Character.toUpperCase(s.charAt(0)));
        return stringBuilder.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("MyApp", getClass().toString() + " Request:" + requestCode + " Result:" + resultCode);
        if (requestCode == RC_SIGN_IN) {
            Log.v("MyApp", getClass().toString() + " onActivityResult:If Google");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (requestCode==64206) {
            Log.v("MyApp", getClass().toString() + " onActivityResult:Else If Facebook");
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else if(requestCode==65538) {
            int errorCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());
            Log.v("MyApp", getClass().toString() + " onActivityResult:Else If 65538" + " Error:" +errorCode);
//            (GoogleApiAvailability.getInstance().getErrorDialog(this, errorCode, 10 )).show();
            PendingIntent pendingIntent = GoogleApiAvailability.getInstance().getErrorResolutionPendingIntent(
                    getApplicationContext(), errorCode, 10);

        }

    }

    //Google onConnectionFailed -----------------------------------------------------------------------------------
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("MyApp", getClass().toString() + " ConnectionFailed: " + connectionResult );
        Log.v("MyApp", getClass().toString() + " ConnectionFailed: " + connectionResult.getErrorCode() );
        Log.v("MyApp", getClass().toString() + " ConnectionFailed: " + connectionResult.getErrorMessage() );
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
//        if(connectionResult.getErrorCode()==2){
//            Toast.makeText(getApplicationContext(), "Please update Google Play Services", Toast.LENGTH_LONG).show();
//        }
    }
}
