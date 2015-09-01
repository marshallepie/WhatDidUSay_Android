package com.example.root.whatdidusay;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by root on 21/8/15.
 */
public class Home_Fragment extends Fragment {
    private static View view;
    static Activity act;


    private ListView mListView;
    private ImageView play_btn;
    private ImageView record_btn;
    private ImageView stop_btn;
    private ProgressBar progressBar;
    private TextView textEmpty;
    private RecordingHelpers recordingHelpers;
    private ArrayList<ModelRecording> listRecords;
    private DataBaseHelper db;
    private Handler handler;
    private Runnable runnable;
    private String tempFilePath ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        act = getActivity();



      /*  avalon_regular = Typeface.createFromAsset(act.getAssets(),
                "avalon-plain.ttf");

        avalon_bold = Typeface.createFromAsset(act.getAssets(),
                "Avalon Bold.ttf");*/

        initViews(view);
        initObjects();
        initListners();




        return view;

    }



    private void initViews(View v) {
        mListView = (ListView) v.findViewById(R.id.tracklist);
        play_btn = (ImageView) v.findViewById(R.id.play_btn);
        record_btn = (ImageView) v.findViewById(R.id.record_btn);
        stop_btn = (ImageView) v.findViewById(R.id.stop_btn);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        textEmpty = (TextView) v.findViewById(R.id.textEmpty);
        record_btn.setAlpha(0.5f);
        stop_btn.setAlpha(0.5f);




    }

    private void initObjects() {
        recordingHelpers = new RecordingHelpers();
        db = new DataBaseHelper(getActivity());
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                recordingHelpers.stopRecording();
                recordingHelpers.startRecording(tempFilePath);

            }
        };

        tempFilePath = recordingHelpers.getTempFilePath();

        new FetchDataBase().execute();

    }

    private void initListners() {

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_btn.setAlpha(0.5f);
                record_btn.setAlpha(1.0f);
                stop_btn.setAlpha(1.0f);
                play_btn.setEnabled(false);

              //  recordingHelpers.startRecording(tempFilePath);


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
             //   recordingHelpers.stopRecording();


            }
        });
    }

    private class RecordTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try {
                recordingHelpers.stopRecording();
                recordingHelpers.copyFile(tempFilePath, recordingHelpers.generateFilePath());
                recordingHelpers.startRecording(recordingHelpers.getTempFilePath());

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }



    private class FetchDataBase extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(ProgressBar.VISIBLE);
            textEmpty.setVisibility(TextView.GONE);
            listRecords = null;
            listRecords = new ArrayList<ModelRecording>();


        }

        @Override
        protected Void doInBackground(Void... params) {
            listRecords = db.getAllRecords();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            textEmpty.setVisibility(TextView.VISIBLE);
            mListView.setAdapter(new AdapterRecording(act, listRecords));
            mListView.setEmptyView(textEmpty);
            progressBar.setVisibility(ProgressBar.GONE);

        }
    }
}
