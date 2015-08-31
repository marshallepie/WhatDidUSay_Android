package com.example.root.whatdidusay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dottechnologies on 31/8/15.
 */
public class AdapterRecording extends BaseAdapter{

    private LayoutInflater inflater = null;
    private View previousView;

    private  Context con;

    public AdapterRecording(Context c){
        inflater = LayoutInflater.from(c);

    }

    @Override
    public int getCount() {
        return 15;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

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
        holder.play_tracklist_btn.setTag(position);
        holder.play_tracklist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.play_tracklist_btn.setImageResource(R.drawable.pause_btn);

            }
        });

        return v;
    }

    private class SettingHolder {
        ImageView play_tracklist_btn;
        TextView title_tracklist, date_text, time_text, record_time;

    }
}
