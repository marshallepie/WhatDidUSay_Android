package com.example.root.whatdidusay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by root on 21/8/15.
 */
public class Home_Fragment extends Fragment {

    private static View view;
    private static Activity act;


    private ListView mListView;
    private ImageView play_btn;
    private ImageView record_btn;
    private ImageView stop_btn;
    private ProgressBar progressBar;
    private TextView textEmpty;
    private TextView tExtStatus;
    private RecordingHelpers recordingHelpers;
    private ArrayList<ModelRecording> listRecords;
    private DataBaseHelper db;
    private Handler handler;
    private Runnable runnable;
    private String tempFilePath;
    private final String TAG_NO_ACTION = "No Action";
    private final String TAG_MONITORING = "Monitoring";
    private final String TAG_STORING = "Storing";
    private Prefrences prefs;
    private int recordDuration;
    public ArrayList<String> idsDelete;
    public ArrayList<String> linkDelete;


    private DropboxAPI<AndroidAuthSession> mDBApi;
    private  File fileToUpload;

    private DropBoxHelpers dropBoxHelpers;


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
        tExtStatus = (TextView) v.findViewById(R.id.tExtStatus);
        record_btn.setAlpha(0.5f);
        stop_btn.setAlpha(0.5f);
        play_btn.setEnabled(true);
        record_btn.setEnabled(false);
        stop_btn.setEnabled(false);


    }

    private void initObjects() {
        dropBoxHelpers = new DropBoxHelpers(getActivity());
        AndroidAuthSession session = dropBoxHelpers.buildSession();
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);

        recordingHelpers = new RecordingHelpers();
        db = new DataBaseHelper(act);
        prefs = new Prefrences(act);
        idsDelete = new ArrayList<String>();
        linkDelete = new ArrayList<String>();
        recordDuration = (prefs.getInt(Prefrences.KEY_RECORD_DURATION) * 1000);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                recordingHelpers.stopRecording();
                recordingHelpers.startRecording(tempFilePath);
                recordingHelpers.setTimeStarts();

            }
        };

        tempFilePath = recordingHelpers.getTempFilePath();

        new FetchDataBase().execute();

        //  Toast.makeText(act, "" + recordingHelpers.getCurrentDateTime("dd/MM/yyyy") + "\n" + recordingHelpers.getCurrentDateTime("hh:mm aa"), Toast.LENGTH_LONG).show();

    }

    private void initListners() {

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);


            }
        });
        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new RecordTask().execute();


            }
        });
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_btn.setAlpha(1.0f);
                record_btn.setAlpha(0.5f);
                stop_btn.setAlpha(0.5f);
                play_btn.setEnabled(true);
                record_btn.setEnabled(false);
                stop_btn.setEnabled(false);
                handler.removeCallbacks(runnable);
                recordingHelpers.stopRecording();
                tExtStatus.setText(TAG_NO_ACTION);
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                //   recordingHelpers.stopRecording();


            }
        });
    }

    public void exportDropBox(String path) {

        if (CheckInternet.isNetworkAvailable(getActivity())){

            fileToUpload = new File(path);

            if (fileToUpload.exists()){
                if (mDBApi.getSession().isLinked()){

                    UploadFile uploadFile = new UploadFile(getActivity(), mDBApi, fileToUpload);
                    uploadFile.execute();

                }
                else {
                    mDBApi.getSession().startOAuth2Authentication(getActivity());


                }
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("File not Exist");
                builder.setMessage("File is removed");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
            }


        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("No Connection");
            builder.setMessage("Connect to Internet");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
        }


    }


    @Override
    public void onResume() {
        super.onResume();
        AndroidAuthSession session = mDBApi.getSession();

        // The next part must be inserted in the onResume() method of the
        // activity from which session.startAuthentication() was called, so
        // that Dropbox authentication completes properly.
        if (session.authenticationSuccessful()) {
            try {
                // Mandatory call to complete the auth
                session.finishAuthentication();

                // Store it locally in our app for later use
                dropBoxHelpers.storeAuth(session);

                UploadFile uploadFile = new UploadFile(getActivity(), mDBApi, fileToUpload);
                uploadFile.execute();

            } catch (IllegalStateException e) {
                Toast.makeText(getActivity(), "Couldn't authenticate with Dropbox:" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.i("EROOR", "Error authenticating", e);
            }
        }
    }




    private class RecordTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            record_btn.setEnabled(false);
            progressBar.setVisibility(ProgressBar.VISIBLE);
            recordingHelpers.stopRecording();
            handler.removeCallbacks(runnable);
            tExtStatus.setText(TAG_STORING);


        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String filePath = recordingHelpers.generateFilePath();

                recordingHelpers.copyFile(tempFilePath, filePath);

                ModelRecording models = new ModelRecording();
                models.setName("");
                models.setDate(recordingHelpers.getCurrentDateTime("dd/MM/yyyy"));
                models.setTime(recordingHelpers.getCurrentDateTime("hh:mm aa"));
                models.setDuration(recordingHelpers.getElapseTime());
                models.setPath(filePath);
                db.addRecord(models);


                Log.e("Timer<><><><>", recordingHelpers.getElapseTime());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            record_btn.setEnabled(true);
            recordingHelpers.setTimeStarts();
            recordingHelpers.startRecording(tempFilePath);
            handler.postDelayed(runnable, recordDuration);
            progressBar.setVisibility(ProgressBar.GONE);
            tExtStatus.setText(TAG_MONITORING);
            new FetchDataBase().execute();
        }
    }


    private class FetchDataBase extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(ProgressBar.VISIBLE);
            textEmpty.setVisibility(TextView.GONE);
            listRecords = null;
            listRecords = new ArrayList<ModelRecording>();
            listRecords.clear();
            idsDelete.clear();

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
            mListView.setAdapter(new AdapterRecording(act, listRecords, Home_Fragment.this));
            mListView.setEmptyView(textEmpty);
            progressBar.setVisibility(ProgressBar.GONE);

        }
    }

    public void deleteOperation() {


        if (idsDelete.size() > 0) {
            String allid = idsDelete.toString();
            allid = allid.replace("[", "");
            allid = allid.replace("]", "");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Confirm Delete");
            builder.setMessage("Do you want to delete ?");
            final String finalAllid = allid;
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new DeleteData().execute(finalAllid);
                }
            });
            builder.setNegativeButton(android.R.string.cancel,null);

            builder.show();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Error");
            builder.setMessage("make a selection to delete");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();

        }


    }

    class DeleteData extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {

            for (int i = 0; i < linkDelete.size(); i++) {
                File file = new File(linkDelete.get(i).toString());
                if (file.exists()) {
                    file.delete();
                }
            }
            db.deleteContact(params[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(ProgressBar.GONE);
            new FetchDataBase().execute();
        }
    }

}
