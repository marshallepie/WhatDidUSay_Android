package com.example.root.whatdidusay;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by dottechnologies on 31/8/15.
 */
public class RecordingHelpers {

    private String LOG_TAG = "RecordingHelpers";

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;


    public RecordingHelpers() {

    }

    public String generateFileName() {
        String mFileName;
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + ".What_did_you_say"+File.separator + System.currentTimeMillis() + ".3gp";


        return mFileName;
    }


    private void startRecording(String mFileName) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }


    private void startPlaying(String mFileName) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }


}
