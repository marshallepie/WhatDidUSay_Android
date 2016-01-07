package com.example.root.whatdidusay.Activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.whatdidusay.Helpers.RecordingHelpers;
import com.example.root.whatdidusay.Prefrences;
import com.example.root.whatdidusay.R;

/**
 * Created by dotechws16 on 7/1/16.
 */
public class RecordingActivity extends Activity {

    private ImageView back_btn;
    private TextView title_text;
    private ListView mListView;
    private ImageView play_btn;
    private ImageView record_btn;
    private ImageView stop_btn;
    private ProgressBar progressBar;
    private TextView textEmpty;
    private TextView tExtStatus;
    private Handler handler;
    private Runnable runnable;
    private RecordingHelpers recordingHelpers;
    private int recordDuration;
    private String tempFilePath;
    private final String TAG_NO_ACTION = "No Action";
    private final String TAG_MONITORING = "Monitoring";
    private final String TAG_STORING = "Storing";

    private Prefrences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);


        back_btn = (ImageView) findViewById(R.id.back_btn);
        title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText(getIntent().getStringExtra("TITLE"));
        initListners();
       /* initViews();
        initObjects();
        initListners();*/


    }

    private void initViews() {

        back_btn = (ImageView) findViewById(R.id.back_btn);
        title_text = (TextView) findViewById(R.id.title_text);
        mListView = (ListView) findViewById(R.id.tracklist);
        play_btn = (ImageView) findViewById(R.id.play_btn);
        record_btn = (ImageView) findViewById(R.id.record_btn);
        stop_btn = (ImageView) findViewById(R.id.stop_btn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textEmpty = (TextView) findViewById(R.id.textEmpty);
        tExtStatus = (TextView) findViewById(R.id.tExtStatus);
        record_btn.setAlpha(0.5f);
        stop_btn.setAlpha(0.5f);
        play_btn.setEnabled(true);
        record_btn.setEnabled(false);
        stop_btn.setEnabled(false);


    }

    private void initObjects() {

        title_text.setText(getIntent().getStringExtra("TITLE"));
        recordingHelpers = new RecordingHelpers();
        recordDuration = (prefs.getInt(Prefrences.KEY_RECORD_DURATION) * 1000);
        tempFilePath = recordingHelpers.getTempFilePath();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                recordingHelpers.stopRecording();
                recordingHelpers.startRecording(tempFilePath);
                recordingHelpers.setTimeStarts();

            }
        };


    }

    private void initListners() {

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * playFunction to start recording and disable/enable buttons
     */
    private void playFunction() {
        play_btn.setAlpha(0.5f);
        record_btn.setAlpha(1.0f);
        stop_btn.setAlpha(1.0f);
        play_btn.setEnabled(false);
        record_btn.setEnabled(true);
        stop_btn.setEnabled(true);
        recordingHelpers.startRecording(tempFilePath);
        recordingHelpers.setTimeStarts();
        handler.postDelayed(runnable, recordDuration);
        tExtStatus.setText(TAG_MONITORING);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

    }

    /**
     * stopFunction to stop recording and disable/enable buttons
     */
    private void stopFunction() {

        play_btn.setAlpha(1.0f);
        record_btn.setAlpha(0.5f);
        stop_btn.setAlpha(0.5f);
        play_btn.setEnabled(true);
        record_btn.setEnabled(false);
        stop_btn.setEnabled(false);
        handler.removeCallbacks(runnable);
        recordingHelpers.stopRecording();
        tExtStatus.setText(TAG_NO_ACTION);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

    }


}
