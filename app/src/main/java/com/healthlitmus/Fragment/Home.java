package com.healthlitmus.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.healthlitmus.Helper.GradientOverImageDrawable;
import com.healthlitmus.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements View.OnClickListener {

    TextView textViewLab, textViewDoctor, textViewPatient;
    ImageView imageViewBG;
    Search search;
    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        imageViewBG = (ImageView) view.findViewById(R.id.background_home);
        Bitmap bitmapBG = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        int startColor = Color.argb(175, 0, 0, 0);
        int endColor =Color.argb(175, 0, 0, 0);
        GradientOverImageDrawable gradientOverImageDrawableBG = new GradientOverImageDrawable(getResources(), bitmapBG);
        gradientOverImageDrawableBG.setGradientColors(startColor, endColor);
        imageViewBG.setImageDrawable(gradientOverImageDrawableBG);

        textViewDoctor = (TextView) view.findViewById(R.id.textView_home_doctors);
        textViewLab = (TextView) view.findViewById(R.id.textView_home_labs);
        textViewPatient = (TextView) view.findViewById(R.id.textView_home_patient);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
//        int height = size.y;

        Log.v("MyApp", getClass().toString() + " width " + width );
        width = (width/4)-50 > 150 ? 150: width/4-50 ;

        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.doctor);
//        drawable.setBounds(0, 0, 120, 120);
        drawable.setBounds(0, 0, width, width);
        textViewDoctor.setCompoundDrawables(null, drawable, null, null); // left top right bottum
        textViewDoctor.setOnClickListener(this);

        drawable = ContextCompat.getDrawable(getActivity(), R.drawable.lab);
//        drawable.setBounds(0, 0, 120, 120);
        drawable.setBounds(0, 0, width, width);
        textViewLab.setCompoundDrawables(null, drawable, null, null); // left top right bottum
        textViewLab.setOnClickListener(this);

        drawable = ContextCompat.getDrawable(getActivity(), R.drawable.patient);
//        drawable.setBounds(0, 0, 120, 120);
        drawable.setBounds(0, 0, width, width);
        textViewPatient.setCompoundDrawables(null, drawable, null, null); // left top right bottum
        textViewPatient.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textView_home_doctors:
                Toast.makeText(getActivity(), "Search For Doctors. Yet to be Made", Toast.LENGTH_LONG ).show();
                break;

            case R.id.textView_home_labs:
                search = new Search();
                Toast.makeText(getActivity(), "Search For Labs", Toast.LENGTH_LONG ).show();
                android.support.v4.app.FragmentTransaction fragmentTransaction;
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framelayout_home, search).addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case R.id.textView_home_patient:
                Toast.makeText(getActivity(), "Search For Patient. Yet to be Made", Toast.LENGTH_LONG ).show();
                break;

        }
    }
}
