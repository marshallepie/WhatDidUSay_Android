package com.example.root.whatdidusay.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.root.whatdidusay.Helpers.RecordingHelpers;
import com.example.root.whatdidusay.R;

/**
 * Created by dottechnologies on 31/8/15.
 */
public class MainActivity extends AppCompatActivity{

    private View mViewActionbar;
    private ListView mListView;
    private ImageView play_btn;
    private ImageView record_btn;
    private ImageView stop_btn;
    private RecordingHelpers recordingHelpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        getSupportActionBar().hide();

        initViews();
        initListners();
        initObjects();



    }




    private void initViews() {
        mListView = (ListView) findViewById(R.id.tracklist);
        play_btn = (ImageView) findViewById(R.id.play_btn);
        record_btn = (ImageView) findViewById(R.id.record_btn);
        stop_btn = (ImageView) findViewById(R.id.stop_btn);
        record_btn.setAlpha(0.5f);
        stop_btn.setAlpha(0.5f);


    }

    private void initListners() {

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_btn.setAlpha(0.5f);
                record_btn.setAlpha(1.0f);
                stop_btn.setAlpha(1.0f);
                play_btn.setEnabled(false);

            }
        });
        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_btn.setAlpha(1.0f);
                record_btn.setAlpha(0.5f);
                stop_btn.setAlpha(0.5f);
                play_btn.setEnabled(true);

            }
        });
    }

    private void initObjects() {
        recordingHelpers = new RecordingHelpers();

       // mListView.setAdapter(new AdapterRecording(MainActivity.this));

    }


}
