package com.example.root.whatdidusay.Helpers;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.root.whatdidusay.R.drawable.play_btn;

/**
 * Created by dottechnologies on 31/8/15.
 * THis class is a helper class for recording .
 */
public class RecordingHelpers {

    private String LOG_TAG = "RecordingHelpers";

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    private final String TEMP_FOLDER = "";
    private final String mainDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
            + ".What_did_you_say";
    private final String tempDir = mainDir+File.separator+"Temp";
    private long startTime;


    public RecordingHelpers() {
        File dirFile = new File(mainDir);
        File tempDirFile = new File(tempDir);
        if (!dirFile.exists()){
            dirFile.mkdirs();
        }
        if (!tempDirFile.exists()){
            tempDirFile.mkdirs();
        }

    }

    public String generateFilePath() {
        String mFileName;
        mFileName = mainDir +File.separator + System.currentTimeMillis() + ".3gp";
        return mFileName;
    }

    public String getTempFilePath(){

        String mFileName;
        mFileName = tempDir +File.separator+"temp.3gp";

        return mFileName;


    }


    public void startRecording(String mFileName) {
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

    public void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }


    public void startPlaying(String mFileName, final ImageView playImage) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playImage.setImageResource(play_btn);
                }
            });
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    public void moveIn (String pathInternal, String pathExternal) {
        File fInternal = new File (pathInternal);
        File fExternal = new File (pathExternal);
        if (fInternal.exists()) {
            fInternal.renameTo(fExternal);
        }
    }

    public  void copyFile(String sourceFilePath, String destFilePath) throws IOException {

        File oldFile = new File (sourceFilePath);
        File newFile = new File(destFilePath);
        oldFile.renameTo(newFile);

    }
    public String getCurrentDateTime(String format){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(cal.getTime());
    }

    public void setTimeStarts(){
        startTime = 0;
        startTime = System.currentTimeMillis();
    }
    public String getElapseTime(){
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - startTime;
        double elapsedSeconds = tDelta / 1000.0;
        String timeString;
        timeString = elapsedSeconds<10?"0"+(int)elapsedSeconds:""+(int)elapsedSeconds;
        return "00:"+timeString;
    }



}
