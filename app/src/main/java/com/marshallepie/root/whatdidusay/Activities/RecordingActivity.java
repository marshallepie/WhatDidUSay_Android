package com.marshallepie.root.whatdidusay.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.marshallepie.root.whatdidusay.Adapters.AdapterRecordingFolder;
import com.marshallepie.root.whatdidusay.Helpers.CheckInternet;
import com.marshallepie.root.whatdidusay.Database.DataBaseHelper;
import com.marshallepie.root.whatdidusay.DropBoxHelpers.DropBoxHelpers;
import com.marshallepie.root.whatdidusay.Helpers.RecordingHelpers;
import com.marshallepie.root.whatdidusay.Interfaces.EarButtonInterface;
import com.marshallepie.root.whatdidusay.Models.ModelRecording;
import com.marshallepie.root.whatdidusay.Helpers.Prefrences;
import com.marshallepie.root.whatdidusay.R;
import com.marshallepie.root.whatdidusay.Helpers.UploadFile;
import com.marshallepie.root.whatdidusay.Utils.IabHelper;
import com.marshallepie.root.whatdidusay.Utils.IabResult;
import com.marshallepie.root.whatdidusay.Utils.Inventory;
import com.marshallepie.root.whatdidusay.Utils.Purchase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dottechnologies on 7/1/16.
 */

public class RecordingActivity extends Activity implements EarButtonInterface {

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


    AdapterRecordingFolder adapterRecordingFolder;

    private Prefrences prefs;
    private DataBaseHelper db;
    private ArrayList<ModelRecording> listRecords;

    public ArrayList<String> idsDelete;
    public ArrayList<String> linkDelete;
    private String folderName;

    private File fileToUpload;
    private DropboxAPI<AndroidAuthSession> mDBApi;
    private DropBoxHelpers dropBoxHelpers;

    //com.marshallepie.root.whatdidusay

    //In app purchase
    private static final String TAG =
            "com.example.root.whatdidusay.Activities.RecordingActivity";
    IabHelper mHelper;
    static String ITEM_SKU = "com.marshallepie.root.whatdidusay";
    static final String ITEM_SKU_50_SNIPPETS = "sku.50.snippets";
    static final String ITEM_SKU_200_SNIPPETS = "sku.200.snippets";
    static final String ITEM_SKU_UNLIMITED_SNIPPETS = "sku.unlimited.snippets";

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener;
    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener;
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener;

    private AlertDialog dialogSubscription;
    private AlertDialog.Builder builderSubscription;

    private TextView textPlan1Title;
    private TextView textPlan2Title;
    private TextView textPlan3Title;

    private TextView textPlan1Description;
    private TextView textPlan2Description;
    private TextView textPlan3Description;

    private TextView textPlan1Price;
    private TextView textPlan2Price;
    private TextView textPlan3Price;

    private LinearLayout overlayingLinearLayout;
    private ImageButton overlayingstopImageButton,earButton;
    private ImageView overlayingListeningImageView,overlayingBigIconImageView;
    private TextView overlayingTapToStopTextView;
    OverlayingViewsClickListener listener;

    int dialogCount=0;

    private int radioPlanSelection = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        initViews();
        initObjects();
        initListners();

        playFunction(1);

    }

    private void initViews() {
        recordingHelpers = new RecordingHelpers();

        overlayingLinearLayout = (LinearLayout) findViewById(R.id.overlayingLinearLayout);
        overlayingListeningImageView = (ImageView) findViewById(R.id.imageViewListeningOverlaying);
        overlayingstopImageButton = (ImageButton) findViewById(R.id.imageButtonOverlayingStop);
        overlayingTapToStopTextView = (TextView) findViewById(R.id.overlayingTapToListenTextView);
        overlayingBigIconImageView = (ImageView) findViewById(R.id.overlayingBigIcon);
        earButton = (ImageButton) findViewById(R.id.earButton);


        back_btn = (ImageView)              findViewById(R.id.back_btn);
        title_text = (TextView)             findViewById(R.id.title_text);
        mListView = (SwipeMenuListView)     findViewById(R.id.tracklist);
        play_btn = (ImageView)              findViewById(R.id.play_btn);
        record_btn = (ImageView)            findViewById(R.id.record_btn);
        stop_btn = (ImageView)              findViewById(R.id.stop_btn);
        progressBar = (ProgressBar)         findViewById(R.id.progressBar);
        textEmpty = (TextView)              findViewById(R.id.textEmpty);
        tExtStatus = (TextView)             findViewById(R.id.tExtStatus);
        delete_btn = (ImageView)            findViewById(R.id.delete_btn);
        record_btn.setAlpha(0.5f);
        stop_btn.setAlpha(0.5f);
        play_btn.setEnabled(true);
        record_btn.setEnabled(false);
        stop_btn.setEnabled(false);

        listener = new OverlayingViewsClickListener();
        overlayingTapToStopTextView.setOnClickListener(listener);
        overlayingstopImageButton.setOnClickListener(listener);
        overlayingListeningImageView.setOnClickListener(listener);
        overlayingBigIconImageView.setOnClickListener(listener);
        overlayingLinearLayout.setOnClickListener(listener);

        earButton.setOnClickListener(listener);   // earButton is not in Overlaying layout
    }

    @Override
    public void disableEarButton() {
        earButton.setEnabled(false);
    }

    @Override
    public void enableEarButton() {
        earButton.setEnabled(true);
    }


    class OverlayingViewsClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imageViewListeningOverlaying:
                    earButton.setVisibility(View.GONE);
                    record_btn.performClick();
                    break;
                case R.id.imageButtonOverlayingStop:
                    stop_btn.performClick();
                    overlayingLinearLayout.setVisibility(View.GONE);
                    earButton.setVisibility(View.VISIBLE);
                    break;
                case R.id.overlayingTapToListenTextView:
                    record_btn.performClick();
                    earButton.setVisibility(View.GONE);
                    break;
                case R.id.overlayingBigIcon:
                    record_btn.performClick();
                    earButton.setVisibility(View.GONE);
                    break;

                case R.id.overlayingLinearLayout:
                    record_btn.performClick();
                    earButton.setVisibility(View.GONE);
                    break;
                case R.id.earButton:
                    if (!earButton.isEnabled()) {
                        Toast.makeText(RecordingActivity.this,
                                "Kindly stop the audio first",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                    if(earButton.isEnabled()) {
                        playFunction(1);
                        overlayingLinearLayout.setVisibility(View.VISIBLE);
                        earButton.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }


    private void initObjects() {
        folderName = getIntent().getStringExtra("TITLE");
        title_text.setText(folderName);
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
                handler.postDelayed(runnable, recordDuration);
            }
        };


        dropBoxHelpers = new DropBoxHelpers(RecordingActivity.this);
        AndroidAuthSession session = dropBoxHelpers.buildSession();
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);

        String base64EncodedPublicKey =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsNOQtsf5M5E1WbMD70TVXyAN5CBFgCSK7dJV8DYnu3cRe+3zNbZUgwy6bhULMiCHFAfJwo78EwmlPgEmTk2jdI8AwWh3ut4XGdYn/zlGMu6LQs/dLDdWm8cSO32WldqyPyEEA4UW7XsbvzFYR8K+VNVU18X2ey+WCxA91baXSVEzJ4v3H8hTH79C3EzT6hlAauks3MKooUzV9c6AkEQ6VA36GVmj5D6uDpnclpOlvnV98vIPQexjiGGQI9fboluS5nXZZ/QIyz7eljmtMdDsd2MS7/rZPVVJ7+Ytk2w0oBOL1C0Q0CtOl+8WI8mOcm2IxE4aCWGrtBBSzOuqpGl5nwIDAQAB";
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " + result);
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");
                }
            }
        });
        View viewDialogSubscription = LayoutInflater.from(RecordingActivity.this).inflate(R.layout.layout_dailog_subscription_plan, null);
        builderSubscription = new AlertDialog.Builder(RecordingActivity.this);
        builderSubscription.setView(viewDialogSubscription);
        builderSubscription.setCancelable(false);
        Button buttonCancelSubscription = (Button) viewDialogSubscription.findViewById(R.id.buttonCancelSubscription);
        Button buttonOkSubscription = (Button) viewDialogSubscription.findViewById(R.id.buttonOkSubscription);

        final RadioButton radioPlan1 = (RadioButton) viewDialogSubscription.findViewById(R.id.radio_plan1);
        final RadioButton radioPlan2 = (RadioButton) viewDialogSubscription.findViewById(R.id.radio_plan2);
        final RadioButton radioPlan3 = (RadioButton) viewDialogSubscription.findViewById(R.id.radio_plan3);

        textPlan1Title = (TextView) viewDialogSubscription.findViewById(R.id.text_plan1_title);
        textPlan2Title = (TextView) viewDialogSubscription.findViewById(R.id.text_plan2_title);
        textPlan3Title = (TextView) viewDialogSubscription.findViewById(R.id.text_plan3_title);

        textPlan1Description = (TextView) viewDialogSubscription.findViewById(R.id.text_plan1_description);
        textPlan2Description = (TextView) viewDialogSubscription.findViewById(R.id.text_plan2_description);
        textPlan3Description = (TextView) viewDialogSubscription.findViewById(R.id.text_plan3_description);

        textPlan1Price = (TextView) viewDialogSubscription.findViewById(R.id.text_plan1_price);
        textPlan2Price = (TextView) viewDialogSubscription.findViewById(R.id.text_plan2_price);
        textPlan3Price = (TextView) viewDialogSubscription.findViewById(R.id.text_plan3_price);
        radioPlan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioPlanSelection = 1;
                ITEM_SKU = ITEM_SKU_50_SNIPPETS;
                radioPlan1.setChecked(true);
                radioPlan2.setChecked(false);
                radioPlan3.setChecked(false);
            }
        });
        radioPlan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioPlanSelection = 2;
                ITEM_SKU = ITEM_SKU_200_SNIPPETS;
                radioPlan1.setChecked(false);
                radioPlan2.setChecked(true);
                radioPlan3.setChecked(false);
            }
        });

        radioPlan3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioPlanSelection = 3;
                ITEM_SKU = ITEM_SKU_UNLIMITED_SNIPPETS;
                radioPlan1.setChecked(false);
                radioPlan2.setChecked(false);
                radioPlan3.setChecked(true);
            }
        });


        buttonCancelSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogSubscription.dismiss();
            }
        });

        buttonOkSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioPlan1.isChecked() || radioPlan2.isChecked() || radioPlan3.isChecked()) {
                    dialogSubscription.dismiss();
                    mHelper.launchPurchaseFlow(RecordingActivity.this, ITEM_SKU, 10001,
                            mPurchaseFinishedListener, "mypurchasetoken");
                } else {
                    Toast.makeText(RecordingActivity.this, "Please select Plan", Toast.LENGTH_LONG).show();
                }
            }
        });
      /*  builderSubscription.setPositiveButton(android.R.string.ok, null);
        builderSubscription.setNegativeButton("Unlock", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                                *//*mHelper.launchPurchaseFlow(RecordingActivity.this, ITEM_SKU, 10001,
                                        mPurchaseFinishedListener, "mypurchasetoken");*//*


            }
        });*/

        dialogSubscription = builderSubscription.create();

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
                if (!recordingHelpers.isPlaying)
                    playFunction(0);

            }
        });

        // In app purchase
        mPurchaseFinishedListener
                = new IabHelper.OnIabPurchaseFinishedListener() {
            public void onIabPurchaseFinished(IabResult result,
                                              Purchase purchase) {
                if (result.isFailure()) {
                    // Handle error
                    return;
                } else if (purchase.getSku().equals(ITEM_SKU_50_SNIPPETS)) {

                    // on success  code goes here
                    Log.e(TAG, "mPurchaseFinishedListener");
                    prefs.setBooleanPrefs(Prefrences.KEY_IN_APP_50, true);
                    Toast.makeText(RecordingActivity.this, "Unlocked Limit. Record Up to 50", Toast.LENGTH_LONG).show();
                    // mHelper.queryInventoryAsync(mReceivedInventoryListener);

                } else if (purchase.getSku().equals(ITEM_SKU_200_SNIPPETS)) {

                    prefs.setBooleanPrefs(Prefrences.KEY_IN_APP_200, true);
                    Toast.makeText(RecordingActivity.this, "Unlocked Limit. Record Up to 200", Toast.LENGTH_LONG).show();

                } else if (purchase.getSku().equals(ITEM_SKU_UNLIMITED_SNIPPETS)) {

                    prefs.setBooleanPrefs(Prefrences.KEY_IN_APP_UNLIMITED, true);
                    Toast.makeText(RecordingActivity.this, "Unlocked Limits. Record Unlimited", Toast.LENGTH_LONG).show();

                }

            }
        };

        mReceivedInventoryListener
                = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result,
                                                 Inventory inventory) {

                if (result.isFailure()) {
                    // Handle failure
                } else {
                    Log.e(TAG, "mReceivedInventoryListener");
                    textPlan1Title.setText(inventory.getSkuDetails(ITEM_SKU_50_SNIPPETS).getTitle());
                    textPlan1Description.setText(inventory.getSkuDetails(ITEM_SKU_50_SNIPPETS).getDescription());
                    textPlan1Price.setText(inventory.getSkuDetails(ITEM_SKU_50_SNIPPETS).getPrice());

                    textPlan2Title.setText(inventory.getSkuDetails(ITEM_SKU_200_SNIPPETS).getTitle());
                    textPlan2Description.setText(inventory.getSkuDetails(ITEM_SKU_200_SNIPPETS).getDescription());
                    textPlan2Price.setText(inventory.getSkuDetails(ITEM_SKU_200_SNIPPETS).getPrice());

                    textPlan3Title.setText(inventory.getSkuDetails(ITEM_SKU_UNLIMITED_SNIPPETS).getTitle());
                    textPlan3Description.setText(inventory.getSkuDetails(ITEM_SKU_UNLIMITED_SNIPPETS).getDescription());
                    textPlan3Price.setText(inventory.getSkuDetails(ITEM_SKU_UNLIMITED_SNIPPETS).getPrice());


                    dialogSubscription.show();
                    /*mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                            mConsumeFinishedListener);*/
                }
            }
        };

        mConsumeFinishedListener =
                new IabHelper.OnConsumeFinishedListener() {
                    public void onConsumeFinished(Purchase purchase,
                                                  IabResult result) {

                        if (result.isSuccess()) {

                            //clickButton.setEnabled(true);
                            //on Success goes here
                            prefs.setBooleanPrefs(Prefrences.KEY_IN_APP, true);
                            Log.e(TAG, "mConsumeFinishedListener");
                            Toast.makeText(RecordingActivity.this, "Unlocked Limits", Toast.LENGTH_LONG).show();

                        } else {
                            // handle error
                        }
                    }
                };


        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prefs.getBooleanDefaultFalse(Prefrences.KEY_IN_APP_UNLIMITED)) {
                    new RecordTask().execute();
                    Log.e("KEY_IN_APP_UNLIMITED", "KEY_IN_APP_UNLIMITED");

                } else if (prefs.getBooleanDefaultFalse(Prefrences.KEY_IN_APP_200)) {
                    Log.e("KEY_IN_APP_200", "KEY_IN_APP_200");
                    if (db.getRecordCount() >= 200) {
                        //if (recordingHelpers.isPlaying)
                        // stopFunction();
                        AlertDialog.Builder builder = new AlertDialog.Builder(RecordingActivity.this);
                        builder.setTitle("Limit Exceeds");
                        builder.setCancelable(false);
                        builder.setMessage("Unlock the limit for unlimited recording");
                        builder.setPositiveButton(android.R.string.ok, null);
                        builder.setNegativeButton("Unlock", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*mHelper.launchPurchaseFlow(RecordingActivity.this, ITEM_SKU, 10001,
                                        mPurchaseFinishedListener, "mypurchasetoken");*/
                                List additionalSkuList = new ArrayList();
                                additionalSkuList.add(ITEM_SKU_50_SNIPPETS);
                                additionalSkuList.add(ITEM_SKU_200_SNIPPETS);
                                additionalSkuList.add(ITEM_SKU_UNLIMITED_SNIPPETS);

                                mHelper.queryInventoryAsync(true, additionalSkuList,
                                        mReceivedInventoryListener);

                                //dialogSubscription.show();


                            }
                        });
                        builder.show();
                    } else {
                        new RecordTask().execute();
                    }


                } else if (prefs.getBooleanDefaultFalse(Prefrences.KEY_IN_APP_50)) {
                    Log.e("KEY_IN_APP_50", "KEY_IN_APP_50");
                    if (db.getRecordCount() >= 50) {
                        //if (recordingHelpers.isPlaying)
                        //stopFunction();
                        AlertDialog.Builder builder = new AlertDialog.Builder(RecordingActivity.this);
                        builder.setTitle("Limit Exceeds");
                        builder.setCancelable(false);
                        builder.setMessage("Unlock the limit for unlimited recording");
                        builder.setPositiveButton(android.R.string.ok, null);
                        builder.setNegativeButton("Unlock", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*mHelper.launchPurchaseFlow(RecordingActivity.this, ITEM_SKU, 10001,
                                        mPurchaseFinishedListener, "mypurchasetoken");*/
                                List additionalSkuList = new ArrayList();
                                additionalSkuList.add(ITEM_SKU_50_SNIPPETS);
                                additionalSkuList.add(ITEM_SKU_200_SNIPPETS);
                                additionalSkuList.add(ITEM_SKU_UNLIMITED_SNIPPETS);

                                mHelper.queryInventoryAsync(true, additionalSkuList,
                                        mReceivedInventoryListener);

                            }
                        });
                        builder.show();
                    } else {
                        new RecordTask().execute();
                    }


                } else {
                    Log.e("3", "3");
                    if (db.getRecordCount() >= 3) {


                        //if (recordingHelpers.isPlaying)
                        //stopFunction();
                        AlertDialog.Builder builder = new
                                AlertDialog.Builder(RecordingActivity.this);
                        builder.setTitle("Limit Exceeds");
                        builder.setCancelable(false);
                        builder.setMessage("Unlock the limit for unlimited recording");
                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogCount =0;
                                //play_btn.performClick();
                            }
                        });

                        builder.setNegativeButton("Unlock", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*mHelper.launchPurchaseFlow(RecordingActivity.this, ITEM_SKU, 10001,
                                        mPurchaseFinishedListener, "mypurchasetoken");*/
                                dialogCount =0;

                                List additionalSkuList = new ArrayList();
                                additionalSkuList.add(ITEM_SKU_50_SNIPPETS);
                                additionalSkuList.add(ITEM_SKU_200_SNIPPETS);
                                additionalSkuList.add(ITEM_SKU_UNLIMITED_SNIPPETS);
                                mHelper.queryInventoryAsync(true, additionalSkuList,
                                        mReceivedInventoryListener);
                            }
                        });
                        ++dialogCount;
                        if (dialogCount == 1)
                            builder.show();


                    } else {
                        new RecordTask().execute();
                    }
                }

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
                Log.i("vabhi","StopButon");
                if (recordingHelpers.isPlaying)
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
                                        linkDelete.add(listRecords.get(position).getPath());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("vabhi","mHelper : "+mHelper);

        if (mHelper != null) mHelper.dispose();
        mHelper = null;


        if (recordingHelpers.isRunning()) {
            stopFunction();
        }
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

    public void notifyData() {

        new FetchDataBase().execute();
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
    private void playFunction(int i) {


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
        try {
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
        }catch (Exception e){
            e.printStackTrace();
        }

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
        String fileName;
        String filePath;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDailog = new ProgressDialog(RecordingActivity.this);
            pDailog.setCancelable(false);
            pDailog.setMessage("Storing");
            pDailog.show();
            record_btn.setEnabled(false);

            overlayingTapToStopTextView.setEnabled(false);
            overlayingstopImageButton.setEnabled(false);
            overlayingListeningImageView.setEnabled(false);
            overlayingBigIconImageView.setEnabled(false);
            overlayingLinearLayout.setEnabled(false);
            earButton.setEnabled(false);

            progressBar.setVisibility(ProgressBar.VISIBLE);

            try {
                if (recordingHelpers.isPlaying)
                    recordingHelpers.stopRecording();
            }catch (Exception e){
                e.printStackTrace();
            }

            handler.removeCallbacks(runnable);
            tExtStatus.setText(TAG_STORING);
            //Toast.makeText(getApplicationContext(),""+db.getLastRecordId(),Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                fileName = "AudioNotes-" + (db.getLastRecordId() + 1);
                filePath = recordingHelpers.generateFilePath(folderName, fileName);
                recordingHelpers.copyFile(tempFilePath, filePath);

                MediaMetadataRetriever metaRetriver;
                metaRetriver = new MediaMetadataRetriever();
                metaRetriver.setDataSource(filePath);
                String durationText = metaRetriver
                        .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                Log.e("duration",">>>>>>>>>>>>"+durationText);
                String val = String.valueOf(Integer.parseInt(durationText) / 1000);


                val = Integer.parseInt(val)<10?"0"+(String)val:""+(String)val;
                Log.e("permannent duration",">>>>>>>>>>>>"+val);

                ModelRecording models = new ModelRecording();
                models.setName(fileName);
                models.setDate(recordingHelpers.getCurrentDateTime("dd/MM/yyyy"));
                models.setTime(recordingHelpers.getCurrentDateTime("hh:mm aa"));
                models.setDuration(recordingHelpers.getElapseTime());
                //models.setDuration(val);
                models.setPath(filePath);
                models.setFolder(folderName);
                db.addRecord(models);

                //Log.e("Timer<><><><>", recordingHelpers.getElapseTime());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {


                pDailog.dismiss();

                record_btn.setEnabled(true);
                recordingHelpers.setTimeStarts();
                recordingHelpers.startRecording(tempFilePath);
                handler.postDelayed(runnable, recordDuration);
                progressBar.setVisibility(ProgressBar.GONE);
                tExtStatus.setText(TAG_MONITORING);
                new FetchDataBase().execute();

                overlayingTapToStopTextView.setEnabled(true);
                overlayingstopImageButton.setEnabled(true);
                overlayingListeningImageView.setEnabled(true);
                overlayingBigIconImageView.setEnabled(true);
                overlayingLinearLayout.setEnabled(true);

                earButton.setEnabled(true);   // earButton is not in Overlaying layout
            }catch (Exception e){
                e.printStackTrace();
            }
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
            linkDelete.clear();

        }

        @Override
        protected Void doInBackground(Void... params) {
            listRecords = db.getAllRecordsFromFolder(folderName);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapterRecordingFolder = new AdapterRecordingFolder(RecordingActivity.this, listRecords, RecordingActivity.this);

            textEmpty.setVisibility(TextView.VISIBLE);
            mListView.setAdapter(adapterRecordingFolder);
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
