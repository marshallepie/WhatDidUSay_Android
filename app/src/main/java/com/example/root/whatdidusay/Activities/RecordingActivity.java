package com.example.root.whatdidusay.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.example.root.whatdidusay.Adapters.AdapterRecordingFolder;
import com.example.root.whatdidusay.Helpers.CheckInternet;
import com.example.root.whatdidusay.Database.DataBaseHelper;
import com.example.root.whatdidusay.DropBoxHelpers.DropBoxHelpers;
import com.example.root.whatdidusay.Helpers.RecordingHelpers;
import com.example.root.whatdidusay.Models.ModelRecording;
import com.example.root.whatdidusay.Helpers.Prefrences;
import com.example.root.whatdidusay.R;
import com.example.root.whatdidusay.Helpers.UploadFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dotechws16 on 7/1/16.
 */
public class RecordingActivity extends Activity {

    private ImageView back_btn;
    private TextView title_text;
    private SwipeMenuListView mListView;
    private ImageView play_btn;
    private ImageView record_btn;
    private ImageView stop_btn;
    private ProgressBar progressBar;
    private TextView textEmpty;
    private TextView tExtStatus;
    private ImageView delete_btn;
    private Handler handler;
    private Runnable runnable;
    private RecordingHelpers recordingHelpers;
    private int recordDuration;
    private String tempFilePath;
    private final String TAG_NO_ACTION = "No Action";
    private final String TAG_MONITORING = "Monitoring";
    private final String TAG_STORING = "Storing";

    private Prefrences prefs;
    private DataBaseHelper db;
    private ArrayList<ModelRecording> listRecords;

    public ArrayList<String> idsDelete;
    public ArrayList<String> linkDelete;
    private String folderName;

    private File fileToUpload;
    private DropboxAPI<AndroidAuthSession> mDBApi;
    private DropBoxHelpers dropBoxHelpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);


        initViews();
        initObjects();
        initListners();


    }

    private void initViews() {

        back_btn = (ImageView) findViewById(R.id.back_btn);
        title_text = (TextView) findViewById(R.id.title_text);
        mListView = (SwipeMenuListView) findViewById(R.id.tracklist);
        play_btn = (ImageView) findViewById(R.id.play_btn);
        record_btn = (ImageView) findViewById(R.id.record_btn);
        stop_btn = (ImageView) findViewById(R.id.stop_btn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textEmpty = (TextView) findViewById(R.id.textEmpty);
        tExtStatus = (TextView) findViewById(R.id.tExtStatus);
        delete_btn = (ImageView) findViewById(R.id.delete_btn);
        record_btn.setAlpha(0.5f);
        stop_btn.setAlpha(0.5f);
        play_btn.setEnabled(true);
        record_btn.setEnabled(false);
        stop_btn.setEnabled(false);


    }


    private void initObjects() {

        folderName = getIntent().getStringExtra("TITLE");
        title_text.setText(folderName);
        recordingHelpers = new RecordingHelpers();
        db = new DataBaseHelper(RecordingActivity.this);
        idsDelete = new ArrayList<String>();
        linkDelete = new ArrayList<String>();
        prefs = new Prefrences(RecordingActivity.this);
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


        dropBoxHelpers = new DropBoxHelpers(RecordingActivity.this);
        AndroidAuthSession session = dropBoxHelpers.buildSession();
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);

        new FetchDataBase().execute();

    }

    private void initListners() {

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteOperation();
            }
        });

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playFunction();

            }
        });
        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   if (db.getRecordCount() >= 3) {
                    stopFunction();
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecordingActivity.this);
                    builder.setTitle("Limit Exceeds");
                    builder.setCancelable(false);
                    builder.setMessage("Unlock the limit for unlimited recording");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setNegativeButton("Unlock", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });
                    builder.show();
                } else {
                    new RecordTask().execute();
                }*/

                new RecordTask().execute();

               /* if (listRecords.size() >= 3) {
                    stopFunction();
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecordingActivity.this);
                    builder.setTitle("Limit Exceeds");
                    builder.setCancelable(false);
                    builder.setMessage("Unlock the limit for unlimited recording");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setNegativeButton("Unlock", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {



                        }
                    });

                    builder.show();
                } else {
                    new RecordTask().execute();
                }*/


            }
        });
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopFunction();

            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.delete_bin);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };


        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, final int index) {
                switch (index) {
                    case 0:

                        new android.support.v7.app.AlertDialog.Builder(RecordingActivity.this).setTitle("Confirm Delete")
                                .setMessage("Do you want to delete ?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        new DeleteData().execute(listRecords.get(position).getId());
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show();


                        break;

                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        mListView.setMenuCreator(creator);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                showShareDialog(i);
            }
        });
    }

    private void showShareDialog(final int position) {
        String[] exportMenuArray = {"Text", "Mail", "DropBox"};
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(RecordingActivity.this);
        builder.setTitle("Export Your Recording")
                .setItems(exportMenuArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:


                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(RecordingActivity.this);

                                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                                    sendIntent.setType("text/plain");
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, "What Did You Say App - Recording");
                                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(listRecords.get(position).getPath())));

                                    if (defaultSmsPackageName != null) {
                                        sendIntent.setPackage(defaultSmsPackageName);
                                    }
                                    startActivity(sendIntent);

                                } else {

                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.putExtra("sms_body", "What Did You Say App - Recording");
                                    intent.setData(Uri.parse("smsto:"));
                                    intent.setType("video/*");
                                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(listRecords.get(position).getPath())));
                                    if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Cannot Support MMS", Toast.LENGTH_SHORT).show();
                                    }


                                }


                                break;
                            case 1:
                                try {

                                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                    sendIntent.setType("plain/text");
                                    sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "What Did You Say App - Recording");
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, "");
                                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(listRecords.get(position).getPath())));
                                    startActivity(sendIntent);

                                } catch (Exception e) {
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("text/plain");
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "What Did You Say App - Recording");
                                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(listRecords.get(position).getPath())));
                                    startActivity(Intent.createChooser(intent, "Send email via"));
                                }


                                break;
                            case 2:
                                //Toast.makeText(mContext,""+mList.get(position).getPath(),Toast.LENGTH_LONG).show();
                                exportDropBox(listRecords.get(position).getPath());
                                break;
                        }
                    }
                });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();

    }

    public static int dp2px(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
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

                UploadFile uploadFile = new UploadFile(RecordingActivity.this, mDBApi, fileToUpload);
                uploadFile.execute();

            } catch (IllegalStateException e) {
                Toast.makeText(RecordingActivity.this, "Couldn't authenticate with Dropbox:" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.i("EROOR", "Error authenticating", e);
            }
        }
    }

    /**
     * Delete operation to delete from database
     */
    public void deleteOperation() {


        if (idsDelete.size() > 0) {
            String allid = idsDelete.toString();
            allid = allid.replace("[", "");
            allid = allid.replace("]", "");
            AlertDialog.Builder builder = new AlertDialog.Builder(RecordingActivity.this);
            builder.setTitle("Confirm Delete");
            builder.setMessage("Do you want to delete ?");
            final String finalAllid = allid;
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new DeleteData().execute(finalAllid);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.show();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(RecordingActivity.this);
            builder.setTitle("Error");
            builder.setMessage("make a selection to delete");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();

        }


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

    /**
     * export file to drop box
     *
     * @param path
     */
    public void exportDropBox(String path) {

        {

            if (CheckInternet.isNetworkAvailable(getApplicationContext())) {

                fileToUpload = new File(path);


                if (fileToUpload.exists()) {
                    if (mDBApi.getSession().isLinked()) {

                        UploadFile uploadFile = new UploadFile(RecordingActivity.this, mDBApi, fileToUpload);
                        uploadFile.execute();

                    } else {
                        mDBApi.getSession().startOAuth2Authentication(RecordingActivity.this);


                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecordingActivity.this);
                    builder.setTitle("File not Exist");
                    builder.setMessage("File is removed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                }


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordingActivity.this);
                builder.setTitle("No Connection");
                builder.setMessage("Connect to Internet");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
            }


        }


    }


    /**
     * Store recording in database at background thread
     */
    private class RecordTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pDailog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDailog = new ProgressDialog(RecordingActivity.this);
            pDailog.setCancelable(false);
            pDailog.setMessage("Storing");
            pDailog.show();
            record_btn.setEnabled(false);
            progressBar.setVisibility(ProgressBar.VISIBLE);
            recordingHelpers.stopRecording();
            handler.removeCallbacks(runnable);
            tExtStatus.setText(TAG_STORING);
            //Toast.makeText(getApplicationContext(),""+db.getLastRecordId(),Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String fileName = "AudioNotes-" + (db.getLastRecordId() + 1);
                String filePath = recordingHelpers.generateFilePath(folderName, fileName);
                recordingHelpers.copyFile(tempFilePath, filePath);
                ModelRecording models = new ModelRecording();
                models.setName(fileName);
                models.setDate(recordingHelpers.getCurrentDateTime("dd/MM/yyyy"));
                models.setTime(recordingHelpers.getCurrentDateTime("hh:mm aa"));
                models.setDuration(recordingHelpers.getElapseTime());
                models.setPath(filePath);
                models.setFolder(folderName);
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
            pDailog.dismiss();
            record_btn.setEnabled(true);
            recordingHelpers.setTimeStarts();
            recordingHelpers.startRecording(tempFilePath);
            handler.postDelayed(runnable, recordDuration);
            progressBar.setVisibility(ProgressBar.GONE);
            tExtStatus.setText(TAG_MONITORING);
            new FetchDataBase().execute();
        }
    }

    /**
     * fetch all recording data from database in background thread
     */
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

            listRecords = db.getAllRecordsFromFolder(folderName);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            textEmpty.setVisibility(TextView.VISIBLE);
            mListView.setAdapter(new AdapterRecordingFolder(RecordingActivity.this, listRecords, RecordingActivity.this));
            mListView.setEmptyView(textEmpty);
            progressBar.setVisibility(ProgressBar.GONE);

        }
    }

    /**
     * Delete all Selected Recording from database in background thread
     */
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
