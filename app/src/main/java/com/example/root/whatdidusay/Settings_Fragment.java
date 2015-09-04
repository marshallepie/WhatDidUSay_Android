package com.example.root.whatdidusay;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by root on 21/8/15.
 * This class is to set timing for recording
 */
public class Settings_Fragment extends Fragment {

    private static View view;
    static Activity act;
    private SeekBar record_seekbar;
    private TextView text_seek_bar;
    private Prefrences prefs;
    private int step = 1;
    private int max = 30;
    private int min = 10;
    private int valueDurationPrefs;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_fragment, container, false);
        act = getActivity();

        initViews(view);
        initObjects();
        initListners();
        return view;

    }

    private void initViews(View view) {

        record_seekbar = (SeekBar) view.findViewById(R.id.record_seekbar);
        text_seek_bar = (TextView) view.findViewById(R.id.text_seek_bar);

    }
    private void initObjects() {
        prefs = new Prefrences(getActivity());
        record_seekbar.setMax((max - min) / step);
        valueDurationPrefs = prefs.getInt(Prefrences.KEY_RECORD_DURATION);
        record_seekbar.setProgress((valueDurationPrefs-min));
        text_seek_bar.setText("Duration: " + valueDurationPrefs);
    }
    private void initListners() {

        record_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double value = min + (progress * step);
                prefs.setIntPrefs(Prefrences.KEY_RECORD_DURATION,(int)value);
                text_seek_bar.setText("Duration: "+(int)value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });
    }




}
