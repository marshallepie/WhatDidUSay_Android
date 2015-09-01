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

    private final String TEMP_FOLDER = "";
    private final String mainDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
            + ".What_did_you_say";
    private final String tempDir = mainDir+File.separator+"Temp";

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


    public void startPlaying(String mFileName) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
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


/*
        File sourceFile = new File(sourceFilePath);
        File destFile = new File(destFilePath);

        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new RandomAccessFile(sourceFile,"rw").getChannel();
            destination = new RandomAccessFile(destFile,"rw").getChannel();

            long position = 0;
            long count    = source.size();

            source.transferTo(position, count, destination);
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }*/
    }


}
