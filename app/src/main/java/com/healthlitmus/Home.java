package com.healthlitmus;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.healthlitmus.Helper.MultiSpinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {


    public Home() {
        // Required empty public constructor
    }

    View view;
    Spinner spinnerCity, spinnerArea;
    MultiSpinner multiSpinner;

    String city[] = {"Pick a City","Delhi", "Gurgaon", "Bangalore", "Mumbai"};
    String area[] = {"Pick a Area","Delhi", "Gurgaon", "Bangalore", "Mumbai"};
    String test[] = {"Delhi", "Gurgaon", "Bangalore", "Delhi", "Gurgaon", "Bangalore", "Delhi", "Gurgaon", "Bangalore",
            "Delhi", "Gurgaon", "Bangalore", "Delhi", "Gurgaon", "Bangalore", "Delhi", "Gurgaon", "Bangalore", "Delhi",
            "Delhi", "Gurgaon", "Bangalore", "Delhi", "Gurgaon", "Bangalore", "Delhi", "Gurgaon", "Gurgaon", "Bangalore", "Mumbai" };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        spinnerCity = (Spinner) view.findViewById(R.id.spinner_home_city);
        spinnerArea = (Spinner) view.findViewById(R.id.spinner_home_area);
        multiSpinner = (MultiSpinner) view.findViewById(R.id.spinner_home_test);

        ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, R.id.spinner_text_name, city);
        spinnerCity.setAdapter(adapterCity);

        ArrayAdapter<String> adapterArea = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text,R.id.spinner_text_name, area);
        spinnerArea.setAdapter(adapterArea);

        ArrayList<String> listTest = new ArrayList<>(Arrays.asList(test));
        multiSpinner.setItems(listTest, "Pick One", new MultiSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                Log.v("MyApp", getClass().getEnclosingMethod().toString() + "MultiSpinner Listener ");
                for(int i=0; i<selected.length ; i++ ){
                    if(selected[i]){
                        Log.v("MyApp", getClass().getEnclosingClass().toString() + "  " + test[i]);
                    }
                }
            }
        });

        return view;
    }

}
