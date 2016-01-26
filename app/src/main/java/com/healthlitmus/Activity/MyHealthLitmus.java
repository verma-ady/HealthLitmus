package com.healthlitmus.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.healthlitmus.Helper.AppController;
import com.healthlitmus.Helper.DatabaseManager;
import com.healthlitmus.Helper.GradientOverImageDrawable;
import com.healthlitmus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MyHealthLitmus extends AppCompatActivity implements Animation.AnimationListener,
        GoogleApiClient.OnConnectionFailedListener {

    ImageView imageViewBG;
    Toolbar toolbar;
    EditText editTextFName, editTextLName, editTextEmail, editTextAddress, editTextDOB, editTextPhone;
    Button buttonSubmit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    AutoCompleteTextView autoCompleteTextViewGender;
    private final int DATEPICKER = 0;
    private int DOB_date, DOB_month, DOB_year;
    public Calendar calender;
    ProgressDialog progressDialog;
    Map<String, String> params = new HashMap<String, String>();

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_health_litmus);

        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", getApplicationContext().MODE_PRIVATE);
        editor = sharedPreferences.edit();

        calender = Calendar.getInstance();
        DOB_date = calender.get(Calendar.DATE);
        DOB_month = calender.get(Calendar.MONTH);
        DOB_year = calender.get(Calendar.YEAR);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN)).requestEmail().build();
//        Log.v("MyApp", "onCreate() 1");
        //Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).addApi(Plus.API).build();

        // enabling toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        Drawable dr = getResources().getDrawable(R.drawable.health);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 40, 40, true));
        getSupportActionBar().setLogo(d);

        imageViewBG = (ImageView) findViewById(R.id.background_MyHL);
        Bitmap bitmapBG = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        int startColor = Color.argb(175, 0, 0, 0);
        int endColor =Color.argb(175, 0, 0, 0);
        GradientOverImageDrawable gradientOverImageDrawableBG = new GradientOverImageDrawable(getResources(), bitmapBG);
        gradientOverImageDrawableBG.setGradientColors(startColor, endColor);
        imageViewBG.setImageDrawable(gradientOverImageDrawableBG);

        autoCompleteTextViewGender = (AutoCompleteTextView) findViewById(R.id.autoText_MyHealthLitmus_gender);
        ArrayAdapter<String> arrayAdapterGender = new ArrayAdapter<String>(MyHealthLitmus.this,
                android.R.layout.simple_dropdown_item_1line, new String[]{"Male", "Female", "Other"});
        autoCompleteTextViewGender.setThreshold(1);
        autoCompleteTextViewGender.setAdapter(arrayAdapterGender);

        editTextFName = (EditText) findViewById(R.id.edit_MyHealthLitmus_firstName);
        editTextLName = (EditText) findViewById(R.id.edit_MyHealthLitmus_lastName);
        editTextEmail = (EditText) findViewById(R.id.edit_MyHealthLitmus_email);
        editTextAddress = (EditText) findViewById(R.id.edit_MyHealthLitmus_address);
        editTextPhone = (EditText) findViewById(R.id.edit_MyHealthLitmus_phone);

        editTextDOB = (EditText) findViewById(R.id.edit_MyHealthLitmus_dob);
        buttonSubmit = (Button) findViewById(R.id.button_MyHealthLitmus_submit);

        Log.v("MyApp", getClass().toString() + "fname " + sharedPreferences.getString("fname", null) );
        Log.v("MyApp", getClass().toString() + "lname " + sharedPreferences.getString("lname", null) );
        Log.v("MyApp", getClass().toString() + "email " + sharedPreferences.getString("email", null) );
        Log.v("MyApp", getClass().toString() + "gender " + sharedPreferences.getString("gender", null) );

        editTextFName.setText(sharedPreferences.getString("fname", null));
        editTextLName.setText(sharedPreferences.getString("lname", null));
        editTextEmail.setText(sharedPreferences.getString("email", null) );
        editTextPhone.setText(sharedPreferences.getString("phone", null) );
        editTextAddress.setText(sharedPreferences.getString("address", null));
        editTextDOB.setText(sharedPreferences.getString("dob", null));
        autoCompleteTextViewGender.setText(sharedPreferences.getString("gender", null));

        ViewListener();
        ButtonListener();
    }

    @Override
    public void onBackPressed() {
        if(sharedPreferences.getString("loginVia", null ).equals("google")) {
            if (mGoogleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(getApplicationContext(), "Logged Out of Google ", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else if (sharedPreferences.getString("loginVia", null ).equals("fb")){
            LoginManager.getInstance().logOut();
            Toast.makeText(getApplicationContext(), "Logged Out of Facebook ", Toast.LENGTH_LONG).show();
        }
        editor.remove("fname");
        editor.remove("lname");
        editor.remove("email");
        editor.remove("dob");
        editor.remove("phone");
        editor.remove("address");
        editor.remove("gender");
        editor.commit();
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        if(sharedPreferences.getString("loginVia", null ).equals("google")) {
            if (mGoogleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(getApplicationContext(), "Logged Out of Google ", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else if (sharedPreferences.getString("loginVia", null ).equals("fb")){
            LoginManager.getInstance().logOut();
            Toast.makeText(getApplicationContext(), "Logged Out of Facebook ", Toast.LENGTH_LONG).show();
        }
        editor.remove("fname");
        editor.remove("lname");
        editor.remove("email");
        editor.remove("dob");
        editor.remove("phone");
        editor.remove("address");
        editor.remove("gender");
        editor.commit();
        super.onStop();
    }

    private void ViewListener() {
        editTextDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATEPICKER);
            }
        });

        editTextAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    SubmitData();
                    return true;
                } else {
                    return false;
                }
            }
        });

        autoCompleteTextViewGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextViewGender.showDropDown();
            }
        });
    }

    private void ButtonListener (){
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitData();
            }
        });
    }

    private void SubmitData(){
        if (editTextFName.length() != 0 && editTextLName.length() != 0 && editTextDOB.length() != 0 && editTextEmail.length() != 0 &&
                autoCompleteTextViewGender.length() != 0 && editTextAddress.length() != 0) {
            if(!isAlpha(editTextFName.getText().toString()) && !isAlpha(editTextLName.getText().toString() )) {
                Toast.makeText(getApplicationContext(), "Name cannot contain Numeric Value", Toast.LENGTH_LONG).show();
            } else if( !checkGender(autoCompleteTextViewGender.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Invalid Gender", Toast.LENGTH_LONG).show();
            } else if (!isValidPhoneNumber(editTextPhone.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Invalid Mobile", Toast.LENGTH_LONG).show();
            } else {
                //code to make post request
                editor.putString("fname",editTextFName.getText().toString());
                editor.putString("lname",editTextLName.getText().toString());
                editor.putString("email",editTextEmail.getText().toString());
                editor.putString("dob", editTextDOB.getText().toString());
                editor.putString("phone", editTextPhone.getText().toString());
                editor.putString("address", editTextAddress.getText().toString());
                editor.putString("gender", autoCompleteTextViewGender.getText().toString());
                editor.commit();

                progressDialog = new ProgressDialog(MyHealthLitmus.this, R.style.MyThemeDialog);
                progressDialog.setMessage("Wait for a moment");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                params.put("name", editTextFName.getText().toString() + " " + editTextLName.getText().toString());
                params.put("email", editTextEmail.getText().toString());
                params.put("birthday", editTextDOB.getText().toString());
                params.put("gender", autoCompleteTextViewGender.getText().toString());
                params.put("phone", editTextPhone.getText().toString());
                params.put("address", editTextAddress.getText().toString());

                volleyPOST("http://healthlitmus.com/patient/preregister.json");
            }
        } else {
            Toast.makeText(getApplicationContext(), "Fill Complete Form", Toast.LENGTH_LONG).show();
//            progressDialog.dismiss();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case DATEPICKER:
                DatePickerDialog datePickerDialog;
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // only for lollipop and newer versions
                     datePickerDialog = new DatePickerDialog(MyHealthLitmus.this,
                            R.style.MyThemeDate, dateSetListener, DOB_year, DOB_month, DOB_date);
                } else {
                    datePickerDialog = new DatePickerDialog(MyHealthLitmus.this,
                            dateSetListener, DOB_year, DOB_month, DOB_date);
                }

                datePickerDialog.getDatePicker().setSpinnersShown(true);
                return datePickerDialog;
            default:return null;
        }
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String dob = dayOfMonth + "/" + Integer.toString(monthOfYear+1) + "/" + year ;
            editTextDOB.setText(dob);
        }
    };

    private void volleyPOST(String url){
        String tag_json_obj = "json_obj_req";

        StringRequest req = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("MyApp", getClass().toString() + " Response Post Request : " + response );
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject user = jsonObject.getJSONObject("user");
                            editor.putString("id", user.getString("_id"));
                            editor.putBoolean("login", true);
                            editor.commit();
                            Intent intent = new Intent(MyHealthLitmus.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("MyApp", getClass().toString() + " caught JSON error");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("MyApp", getClass().toString() + " Error Post Request : " + error.toString() );
                progressDialog.dismiss();
//                Intent intent = new Intent(MyHealthLitmus.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("access-token", "");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(req, tag_json_obj);
    }//

    private boolean isAlpha( String s ){ // return true if only letters
        for (char c: s.toCharArray() ) {
            if( Character.isDigit(c) ){
                return false;
            }
        }
        return true;
    }

    private boolean checkGender (String s ){ // return true if valid gender
        if(s.equals("Male") || s.equals("Female") || s.equals("Other")){
            return true;
        } else {
            return false;
        }
    }

    private static boolean isValidPhoneNumber(String mobile) { //return true if valid 10 digitt number
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

//    @Override
//    public void onConnected(Bundle bundle) {
//        Log.v("MyApp",getClass().toString() +"onConnected");
//        if (ShopRunDataStore.LoginGoogle) {
//            Log.v("MyApp",getClass().toString() + " Google logged in");
//            Toast.makeText(this, "User is connected to Google+", Toast.LENGTH_LONG).show();
//            // Get user's information
//            getProfileInformation();
//        } else {
//            Log.v("MyApp",getClass().toString() +"In if condition to log off");
//            if (mGoogleApiClient.isConnected()) {
//                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//                mGoogleApiClient.disconnect();
//                // mGoogleApiClient.connect();
//            }
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
}
