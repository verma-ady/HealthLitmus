package com.healthlitmus.Fragment;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.healthlitmus.Helper.AppController;
import com.healthlitmus.Helper.GradientOverImageDrawable;
import com.healthlitmus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Search extends Fragment implements Animation.AnimationListener{


    public Search() {
        // Required empty public constructor
    }

    View view;
    AutoCompleteTextView actvCity, actvArea;
    MultiAutoCompleteTextView mactvTest;
    ProgressDialog progressDialog;
    String cityArray[] = {""};
    int cityID[], citySelected;
    String areaArray[] = {""};
    int areaID[], areaSelected;
    String testArray[] = {};
    int testID[], testSelected[];
    boolean cityLoaded = false, areaLoaded=false ;
    Button button;
    ImageView imageViewBG;
    Animation animationButton;
    TestResult testResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_search, container, false);

        imageViewBG = (ImageView) view.findViewById(R.id.background_search);
        Bitmap bitmapBG = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        int startColor = Color.argb(175, 0, 0, 0);
        int endColor =Color.argb(175, 0, 0, 0);
        GradientOverImageDrawable gradientOverImageDrawableBG = new GradientOverImageDrawable(getResources(), bitmapBG);
        gradientOverImageDrawableBG.setGradientColors(startColor, endColor);
        imageViewBG.setImageDrawable(gradientOverImageDrawableBG);

        Bitmap bitmapDropDown = BitmapFactory.decodeResource(getResources(), R.drawable.transparent);
        startColor = Color.argb(200, 0, 0, 0);
        endColor =Color.argb(200, 0, 0, 0);
        GradientOverImageDrawable gradientOverImageDrawableDropDown = new GradientOverImageDrawable(getResources(), bitmapDropDown);
        gradientOverImageDrawableDropDown.setGradientColors(startColor, endColor);

        actvCity = (AutoCompleteTextView) view.findViewById(R.id.actv_home_city);
        actvArea = (AutoCompleteTextView) view.findViewById(R.id.actv_home_area);
        mactvTest = (MultiAutoCompleteTextView) view.findViewById(R.id.mactv_home_testDetails);
        actvCity.setDropDownBackgroundDrawable(gradientOverImageDrawableDropDown);
        actvArea.setDropDownBackgroundDrawable(gradientOverImageDrawableDropDown);
        mactvTest.setDropDownBackgroundDrawable(gradientOverImageDrawableDropDown);

        progressDialog = new ProgressDialog(getContext(), R.style.MyThemeDialog);
        progressDialog.setMessage("Wait for a moment");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        button = (Button) view.findViewById(R.id.button_home_submit);

        volleyRequestCity("http://healthlitmus.com/home/city.json");

        ButtonListener();
//        ArrayList<String> listTest = new ArrayList<>(Arrays.asList(testArray));
//        multiSpinner.setItems(listTest, "Select Location First", new MultiSpinner.MultiSpinnerListener() {
//            @Override
//            public void onItemsSelected(boolean[] selected) {
//                Log.v("MyApp", getClass().getEnclosingMethod().toString() + "MultiSpinner Listener ");
//                for(int i=0; i<selected.length ; i++ ){
//                    if(selected[i]){
//                        Log.v("MyApp", getClass().getEnclosingClass().toString() + "  " + testArray[i]);
//                    }
//                }
//            }
//        });

        ACTVListener();

        return view;
    }

    private void ButtonListener(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationButton = AnimationUtils.loadAnimation(getContext(), R.anim.aplha);
                v.startAnimation(animationButton);
                animationButton.setAnimationListener(Search.this);
            }
        });
    }

    private void ACTVListener(){
        actvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actvCity.setThreshold(0);
                actvCity.showDropDown();
            }
        });

        actvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                progressDialog.setCancelable(true);
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();
                if (cityLoaded) {
                    citySelected = searchCityID(actvCity.getText().toString());
                    Log.v("MyApp", getClass().toString() + " " + actvCity.getText() + " " + citySelected);
                    if (citySelected != -1) {
                        volleyRequestArea("http://healthlitmus.com/home/area/" + citySelected + ".json");
                    } else {
                        Toast.makeText(getContext(), "Select a Valid City", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        actvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actvArea.setThreshold(0);
                actvArea.showDropDown();
            }
        });

        actvArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                progressDialog.setCancelable(true);
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();
                if (areaLoaded) {
                    areaSelected = searchAreaID(actvArea.getText().toString());
                    Log.v("MyApp", getClass().toString() + " " + actvArea.getText() + " " + areaSelected);
                    Uri.Builder uri = Uri.parse("http://healthlitmus.com/medicaltest/search.json").buildUpon()
                            .appendQueryParameter("city", Integer.toString(citySelected))
                            .appendQueryParameter("area", Integer.toString(areaSelected));
                    String url = uri.toString();
                    Log.v("MyApp", getClass().toString() + " Test Search URL: " + url);
                    volleyRequestTest(url);
                } else {
                    Toast.makeText(getContext(), "Select a Valid Area", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void volleyRequestCity(String url ){
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";

//        ProgressDialog pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Loading...");
//        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.v("MyApp", getClass().toString() + " JSONArray City Response : " + response.toString());
                        JSONObject cityJSON;
                        cityArray = new String[response.length()];
                        cityID = new int[response.length()];
                        for(int i=0; i<response.length() ; i++ ){
                            try {
                                cityJSON = response.getJSONObject(i);
                                cityID[i] = cityJSON.getInt("id");
                                cityArray[i] = cityJSON.getString("name");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }//for
                        ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(getActivity(), R.layout.custom_text, R.id.custom_text_name, cityArray);
                        actvCity.setAdapter(adapterCity);
                        progressDialog.dismiss();
                        cityLoaded = true;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("MyApp", getClass().toString() + "City Error: " + error.getMessage());
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(req, tag_json_arry);
    }

    private void volleyRequestArea(String url) {
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";

//        ProgressDialog pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Loading...");
//        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.v("MyApp", getClass().toString() + " JSONArray Area Response : " + response.toString());
                        JSONObject area;
                        areaArray = new String[response.length()];
                        areaID = new int[response.length()];

                        for(int i=0; i<response.length() ; i++ ){
                            try {
                                area = response.getJSONObject(i);
                                areaID[i] = area.getInt("id");
                                areaArray[i] = area.getString("name");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }//for

                        ArrayAdapter<String> adapterArea = new ArrayAdapter<String>(getActivity(), R.layout.custom_text,R.id.custom_text_name, areaArray);
                        actvArea.setAdapter(adapterArea);
                        progressDialog.dismiss();
                        areaLoaded = true;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("MyApp", getClass().toString() + " Area Error: " + error.getMessage());
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(req, tag_json_arry);
    }

    private void volleyRequestTest(String url) {
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("MyApp", getClass().toString() + " JSONArray Test Response : " + response.toString());
                        try {
                            JSONArray allTest = response.getJSONArray("alltests");
                            JSONObject test;
                            testArray = new String[allTest.length()];
                            for(int i=0 ; i<allTest.length() ; i++) {
                                test = allTest.getJSONObject(i);
                                testArray[i] = test.getString("_title");
                            }

                            ArrayAdapter<String> adapterTest = new ArrayAdapter<String>(getActivity(), R.layout.custom_text,
                                    R.id.custom_text_name, testArray);
                            mactvTest.setAdapter(adapterTest);
                            mactvTest.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                            progressDialog.dismiss();
                            areaLoaded = true;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("MyApp", getClass().toString() + "Test Error: " + error.getMessage());
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(req, tag_json_arry);
    }

    private int searchCityID( String cityName ){
        for(int i=0 ; i<cityArray.length ; i++ ){
            if(cityArray[i].equals(cityName) ){
                return cityID[i];
            }
        }
        return -1;
    }

    private int searchAreaID( String cityName ){
        for(int i=0 ; i<areaArray.length ; i++ ){
            if(areaArray[i].equals(cityName) ){
                return areaID[i];
            }
        }
        return -1;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == animationButton) {
//            Toast.makeText(getContext(), "Animation Stopped", Toast.LENGTH_SHORT).show();
            testResult = new TestResult();
            android.support.v4.app.FragmentTransaction fragmentTransaction;
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.framelayout_home, testResult).addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
