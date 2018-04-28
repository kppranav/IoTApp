package com.example.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.arduinoesp.R;
import com.example.arduinoesp.RotationGestureDetector;
import com.example.utils.RotaryKnobView;

/**
 * Created by uvionics on 4/2/16.
 */
public class FanFragment extends Fragment {
    ImageView rotor;
    Bitmap bitmap;
    float angle = -180;
    GestureDetectorCompat detector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fan, container, false);

        setupUi(view);
        return view;
    }

    private void setupUi(View view) {

        RotaryKnobView knobView = (RotaryKnobView) view.findViewById(R.id.rotaryKnob);
        knobView.setKnobListener(new RotaryKnobView.RotaryKnobListener() {
            @Override
            public void onKnobChanged(int arg) {
                if (arg > 0) {
                    Log.d("TAG", "rotate right");
                } else {
                    Log.d("TAG", "rotate left");
                }
            }
        });




    }



}
