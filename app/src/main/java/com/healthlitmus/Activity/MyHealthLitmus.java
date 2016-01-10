package com.healthlitmus.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.healthlitmus.Helper.AppController;
import com.healthlitmus.Helper.DatabaseManager;
import com.healthlitmus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MyHealthLitmus extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_health_litmus);

        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", getApplicationContext().MODE_PRIVATE);
        editor = sharedPreferences.edit();

        calender = Calendar.getInstance();
        DOB_date = calender.get(Calendar.DATE);
        DOB_month = calender.get(Calendar.MONTH);
        DOB_year = calender.get(Calendar.YEAR);
        toolbar = (Toolbar) findViewById(R.id.toolbar_MyHealthLitmus);

        // enabling toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        Drawable dr = getResources().getDrawable(R.drawable.health);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 40, 40, true));
        getSupportActionBar().setLogo(d);

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

        editTextFName.setText(sharedPreferences.getString("fname", null));
        editTextLName.setText(sharedPreferences.getString("lname", null) );
        editTextEmail.setText(sharedPreferences.getString("email", null) );

        ViewListener();
        SubmitButton();
    }

    private void ViewListener(){
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
                    SubmitButton();
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

    private void SubmitButton (){
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextFName.length() != 0 && editTextLName.length() != 0 && editTextDOB.length() != 0 && editTextEmail.length() != 0 &&
                        autoCompleteTextViewGender.length() != 0 && editTextAddress.length() != 0) {
                    //code to make post request
                    editor.putString("dob", editTextDOB.getText().toString());
                    editor.putString("phone", editTextPhone.getText().toString());
                    editor.putString("address", editTextAddress.getText().toString());
                    editor.putString("gender", autoCompleteTextViewGender.getText().toString());
                    editor.commit();
                    progressDialog = new ProgressDialog(getApplicationContext());
                    progressDialog.setMessage("Wait for a moment");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    volleyPOST("http://healthlitmus.com/patient/preregister.json");
                } else {
                    Toast.makeText(getApplicationContext(), "Fill Complete Form", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case DATEPICKER:
                DatePickerDialog datePickerDialog = new DatePickerDialog(MyHealthLitmus.this, dateSetListener, DOB_year, DOB_month, DOB_date);
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

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("MyApp", getClass().toString() + " Response Post Request : " + response.toString() );
                        try {
                            progressDialog.dismiss();
                            editor.putString("id", response.getJSONObject("user").getString("_id"));
                            editor.commit();
                            Intent intent = new Intent(MyHealthLitmus.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("MyApp", getClass().toString() + " Error Post Request : " + error.toString() );
                progressDialog.dismiss();
                Intent intent = new Intent(MyHealthLitmus.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", editTextFName.getText().toString() + " " + editTextLName.getText().toString());
                params.put("email", editTextEmail.getText().toString());
                params.put("birthday", editTextDOB.getText().toString());
                params.put("gender", autoCompleteTextViewGender.getText().toString());
                params.put("phone", editTextPhone.getText().toString());
                params.put("address", editTextAddress.getText().toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Content-Language", "en-US");
                headers.put("access-token", "");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(req, tag_json_obj);

    }//
}
