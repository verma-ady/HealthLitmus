package com.healthlitmus.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.healthlitmus.Helper.ConnectionDetector;
import com.healthlitmus.Helper.CustomTypeFaceSpan;
import com.healthlitmus.Helper.GradientOverImageDrawable;
import com.healthlitmus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;

    ImageView imageViewBG;
    Button buttonLogInHealthLitmus, buttonLoginGooglePlus;
    Animation animationButtonAlpha, animationTextViewFade;
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
        loginButton = (LoginButton) findViewById(R.id.buttonRegisterFacebook);
        textViewNewUser = (TextView) findViewById(R.id.text_login_newuser);

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
        loginButton.setCompoundDrawables(drawable, null, null, null);

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
                       Intent intent = new Intent(Login.this, AlreadyUserLogin.class);
                        startActivity(intent);
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
                FBLogin();
            }
        });

        buttonLoginGooglePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    // Google Sign In ---------------------------------------------------------------------------------------
    private void signIn() {
//        Log.v("MyApp", "signIn Login");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.v("MyApp", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            Log.v("MyApp", getClass().toString() + "Google handleSignInResult() if Login");
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
            Toast.makeText(this, "Connect to Internet to Sign In", Toast.LENGTH_SHORT).show();
        }
    }
    //--------------------------------------------------------------------------------------- Google Sign In

    // FaceBook Sign In ---------------------------------------------------------------------------------------
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
            Log.v("MyApp", getClass().toString() + "Login updateWithToken null ");
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
        if (requestCode == RC_SIGN_IN) {
            Log.v("MyApp", getClass().toString() + " onActivityResult If ");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else{
            Log.v("MyApp", getClass().toString() + " onActivityResult Else ");
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Google onConnectionFailed -----------------------------------------------------------------------------------
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("MyApp", "onConnectionFailed:" + connectionResult);
    }
}
