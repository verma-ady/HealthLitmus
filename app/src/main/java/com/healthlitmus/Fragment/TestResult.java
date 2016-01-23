package com.healthlitmus.Fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.healthlitmus.Helper.AppController;
import com.healthlitmus.Helper.ContentTestResult;
import com.healthlitmus.Helper.CustomLinearLayoutManager;
import com.healthlitmus.Helper.GradientOverImageDrawable;
import com.healthlitmus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestResult extends Fragment {


    public TestResult() {
        // Required empty public constructor
    }

    private ContentTestResult contentTestResult;
    private RVAdapter rvAdapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;
    ImageView imageViewBG;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_result, container, false);

        imageViewBG = (ImageView) view.findViewById(R.id.background_test);
        Bitmap bitmapBG = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        int startColor = Color.argb(175, 0, 0, 0);
        int endColor =Color.argb(175, 0, 0, 0);
        GradientOverImageDrawable gradientOverImageDrawableBG = new GradientOverImageDrawable(getResources(), bitmapBG);
        gradientOverImageDrawableBG.setGradientColors(startColor, endColor);
        imageViewBG.setImageDrawable(gradientOverImageDrawableBG);

        contentTestResult = new ContentTestResult();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_card_test);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setMessage("Wait for a moment");
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//        volleyRequestTest("http://healthlitmus.com/medicaltest/appsearch.json?city=3&area=7680&test%5B%5D=24");
        contentTestResult.clear();
        contentTestResult.addItem(new ContentTestResult.DummyItem("XYZ Labs", "CCD", 10));
//        contentTestResult.addItem(new ContentTestResult.DummyItem("EFG", "MNO", 2));

        rvAdapter=new RVAdapter(contentTestResult.ITEMS);
        recyclerView.setAdapter(rvAdapter);

        return view;
    }

    private class RVAdapter extends RecyclerView.Adapter<RVAdapter.CardViewHolder> {
        ContentTestResult content = new ContentTestResult();

        public RVAdapter ( List<ContentTestResult.DummyItem> list_dummy ){
            content.ITEMS = list_dummy;
        }

        @Override
        public RVAdapter.CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_test_result, parent, false );
            CardViewHolder cardViewHolder = new CardViewHolder( view );
            return cardViewHolder;
        }

        @Override
        public void onBindViewHolder(RVAdapter.CardViewHolder holder, int position) {
            holder.place.setText(content.ITEMS.get(position).place);
            holder.near.setText("Near " + content.ITEMS.get(position).near);
            holder.price.setText("Rs. " + content.ITEMS.get(position).price);
        }

        @Override
        public int getItemCount() {
            return content.ITEMS.size();
        }

        public class CardViewHolder extends RecyclerView.ViewHolder {
            CardView cardView;
            TextView place, near, price;

            public CardViewHolder(View itemView) {
                super(itemView);
                cardView = (CardView) itemView.findViewById(R.id.cardView_card_test);
                place = (TextView) itemView.findViewById(R.id.text_card_test_place);
                near = (TextView) itemView.findViewById(R.id.text_card_test_near);
                price = (TextView) itemView.findViewById(R.id.text_card_test_price);
            }
        }
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
                            JSONArray jsonArray = response.getJSONArray("results");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject result = jsonArray.getJSONObject(i);
                                contentTestResult.addItem(new ContentTestResult.DummyItem( result.getString("org_name"),
                                        result.getString("street"), result.getInt("charge")));
                            }
                        } catch ( JSONException e ){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
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


}
