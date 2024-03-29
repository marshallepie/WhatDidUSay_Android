package com.marshallepie.root.whatdidusay.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.marshallepie.root.whatdidusay.Helpers.Prefrences;
import com.marshallepie.root.whatdidusay.R;

/**
 * Created by dottechnologies on 31/8/15.
 * This class is to show welcome screen for 3 seconds and the navigate to SliderActivity
 */
public class SplashActivity extends Activity {
    private Prefrences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();

            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
        }

        prefs = new Prefrences(SplashActivity.this);


        if (!prefs.getContains(Prefrences.KEY_PREFRENCE_INIT)) {
            prefs.setBooleanPrefs(Prefrences.KEY_PREFRENCE_INIT, true);
            prefs.setIntPrefs(Prefrences.KEY_RECORD_DURATION, 10);
            prefs.setBooleanPrefs(Prefrences.KEY_IN_APP, false);
            prefs.setIntPrefs(Prefrences.KEY_FIRST_TIME_DILAOG,0);
            //prefs.setIntPrefs(Prefrences.KEY_RECORD_ID,0);

        }


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, SliderActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, 3000);





    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
