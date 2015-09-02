package com.example.root.whatdidusay;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.root.whatdidusay.R.drawable.play_btn;

/**
 * Created by Dottechnologies on 31/8/15.
 * This class is a bridge to set data to the list and delete and playing functionality is performed in this class
 */
public class AdapterRecording extends BaseAdapter {

    private LayoutInflater inflater = null;
    private View previousView;
    private DataBaseHelper db;
    private Context mContext;
    private ArrayList<ModelRecording> mList;
    private RecordingHelpers recordingHelpers;
    private Boolean statusPlaying;
    private int idSongPlaying;
    private MediaPlayer mPlayer = null;

    public AdapterRecording(Context c, ArrayList<ModelRecording> list) {
        mContext = c;
        inflater = LayoutInflater.from(c);
        mList = list;
        statusPlaying = false;
        db = new DataBaseHelper(mContext);
        recordingHelpers = new RecordingHelpers();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        final SettingHolder holder;
        holder = new SettingHolder();

        v = inflater.inflate(R.layout.track_list_custom, null);

        holder.play_tracklist_btn = (ImageView) v
                .findViewById(R.id.play_tracklist_btn);
        holder.button_delete = (ImageView) v.findViewById(R.id.button_delete);
        holder.title_tracklist = (TextView) v
                .findViewById(R.id.title_tracklist);
        //  holder.title_tracklist.setTypeface(avalon_bold);
        holder.date_text = (TextView) v.findViewById(R.id.date_text);
        //   holder.date_text.setTypeface(avalon_regular);
        holder.time_text = (TextView) v.findViewById(R.id.time_text);
        //  holder.time_text.setTypeface(avalon_regular);
        holder.record_time = (TextView) v.findViewById(R.id.record_time);
        //   holder.record_time.setTypeface(avalon_regular);

        holder.title_tracklist.setTag(position);
        holder.title_tracklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  int pos = Integer.parseInt(v.getTag().toString());
                showInputDialog(pos);*/

            }
        });
        holder.play_tracklist_btn.setTag(position);
        holder.play_tracklist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Integer pos = Integer.parseInt(v.getTag().toString());


                if(statusPlaying){
                    //true
                    if (pos == idSongPlaying){
                        statusPlaying = false;
                        if (mList.get(pos).isPlaying()) {
                            stopPlaying();
                            mList.get(idSongPlaying).setIsPlaying(false);
                            holder.play_tracklist_btn.setImageResource(R.drawable.play_btn);
                        }
                    }
                }
                else {
                    //false
                    statusPlaying = true;
                    idSongPlaying = pos;
                    holder.play_tracklist_btn.setImageResource(R.drawable.pause_btn);
                    mList.get(idSongPlaying).setIsPlaying(true);
                    startPlaying(mList.get(idSongPlaying).getPath(), holder.play_tracklist_btn);


                }*/



            }
        });
        holder.button_delete.setTag(position);
        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Integer pos = Integer.parseInt(v.getTag().toString());
                if (mList.get(pos).isForDelete()) {
                    mList.get(pos).setIsForDelete(false);
                    holder.button_delete.setImageResource(R.drawable.ic_delete_uncheck);
                } else {
                    mList.get(pos).setIsForDelete(true);
                    holder.button_delete.setImageResource(R.drawable.ic_delete_check);
                }*/

            }
        });

        if (mList.get(position).isPlaying()) {
            holder.button_delete.setImageResource(R.drawable.pause_btn);
        } else {
            holder.button_delete.setImageResource(R.drawable.play_btn);
        }

        if (mList.get(position).isForDelete()) {
            holder.button_delete.setImageResource(R.drawable.ic_delete_check);
        } else {
            holder.button_delete.setImageResource(R.drawable.ic_delete_uncheck);
        }


        if (!mList.get(position).getName().equals("")) {
            holder.title_tracklist.setText(mList.get(position).getName());
        } else {
            holder.title_tracklist.setText("Record-" + position);
        }


        holder.date_text.setText(mList.get(position).getDate());
        holder.time_text.setText(mList.get(position).getTime());
        holder.record_time.setText(mList.get(position).getDuration());

        return v;
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
                    statusPlaying = false;
                    mList.get(idSongPlaying).setIsPlaying(false);
                }
            });
        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
    }

    public void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private class SettingHolder {
        ImageView play_tracklist_btn, button_delete;
        TextView title_tracklist, date_text, time_text, record_time;

    }

    protected void showInputDialog(int position) {
        final int pos = position;

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View promptView = layoutInflater.inflate(R.layout.layout_dailog_name_edit, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setTitle("File Name");

        final EditText editText = (EditText) promptView.findViewById(R.id.eDitFile);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String editString = editText.getText().toString().trim();
                        if (!editString.equals("")) {

                            db.updateName(mList.get(pos).getId(),editString);
                            mList.get(pos).setName(editString);
                            notifyDataSetChanged();

                        } else {
                            showErrorDialog();
                        }
                        editText.setText(null);
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                editText.setText(null);
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    protected void showErrorDialog() {



        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        alertDialogBuilder.setTitle("Message!");

        alertDialogBuilder.setMessage("Enter Filename");
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  resultText.setText("Hello, " + editText.getText());
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }



}
