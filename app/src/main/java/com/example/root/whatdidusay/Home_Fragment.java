package com.example.root.whatdidusay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by root on 21/8/15.
 */
public class Home_Fragment extends Fragment {
    private static View view;
    static Activity act;
    ListView tracklist;
    ImageView play_btn;
    ImageView record_btn;
    ImageView stop_btn;
    TextView timer_text;
    Typeface avalon_regular, avalon_bold;
    TrackList_Adapter adapter;
    private MediaRecorder myRecorder;
    private MediaPlayer myPlayer;
    private String outputFile = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        act = getActivity();

        getuicontrols();

      /*  avalon_regular = Typeface.createFromAsset(act.getAssets(),
                "avalon-plain.ttf");

        avalon_bold = Typeface.createFromAsset(act.getAssets(),
                "Avalon Bold.ttf");*/

        adapter = new TrackList_Adapter(act);
        tracklist.setAdapter(adapter);

        outputFile = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/javacodegeeksRecording.3gpp";

        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myRecorder.setOutputFile(outputFile);

        return view;

    }

    private void getuicontrols() {
        tracklist = (ListView) view.findViewById(R.id.tracklist);

        play_btn = (ImageView) view.findViewById(R.id.play_btn);
        record_btn = (ImageView) view.findViewById(R.id.record_btn);
        stop_btn = (ImageView) view.findViewById(R.id.stop_btn);

        timer_text = (TextView) view.findViewById(R.id.timer_text);
       // timer_text.setTypeface(avalon_regular);

        play_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                start(v);
            }
        });

    }

    public void start(View view) {
        try {
            myRecorder.prepare();
            myRecorder.start();
        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
            e.printStackTrace();
        }

        play_btn.setEnabled(false);
        record_btn.setEnabled(true);
        stop_btn.setEnabled(true);

       // Toast.makeText(act, "Start recording...", Toast.LENGTH_SHORT).show();
    }

    private class TrackList_Adapter extends BaseAdapter {
        private LayoutInflater inflater = null;
        private View previousView;

        Context con;

        public TrackList_Adapter(Context c) {
            inflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 8;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        private class SettingHolder {
            ImageView play_tracklist_btn;
            TextView title_tracklist, date_text, time_text, record_time;

        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View v = convertView;

            final SettingHolder holder;
            holder = new SettingHolder();

            v = inflater.inflate(R.layout.track_list_custom, null);

            holder.play_tracklist_btn = (ImageView) v
                    .findViewById(R.id.play_tracklist_btn);
            holder.title_tracklist = (TextView) v
                    .findViewById(R.id.title_tracklist);
          //  holder.title_tracklist.setTypeface(avalon_bold);
            holder.date_text = (TextView) v.findViewById(R.id.date_text);
         //   holder.date_text.setTypeface(avalon_regular);
            holder.time_text = (TextView) v.findViewById(R.id.time_text);
          //  holder.time_text.setTypeface(avalon_regular);
            holder.record_time = (TextView) v.findViewById(R.id.record_time);
         //   holder.record_time.setTypeface(avalon_regular);

            return v;
        }

    }
}
