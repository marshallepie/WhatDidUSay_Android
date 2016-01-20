package com.marshallepie.root.whatdidusay.Fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.marshallepie.root.whatdidusay.Activities.RecordingActivity;
import com.marshallepie.root.whatdidusay.Adapters.AdapterFolders;
import com.marshallepie.root.whatdidusay.Database.DataBaseHelper;
import com.marshallepie.root.whatdidusay.Helpers.FileHelpers;
import com.marshallepie.root.whatdidusay.R;

import java.io.File;

/**
 * Created by Dottechnologies on 6/1/16.
 */

public class HomeFragment extends Fragment {

    private SwipeMenuListView listFolders;
    private FileHelpers fileHelpers;
    private ProgressDialog pDailog;
    private File[] folders;
    private AdapterFolders adapterFolders;
    private File[] foldersArray;
    private DataBaseHelper database;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_folders, container, false);
        initViews(view);
        return view;
    }

    /**
     * initialize views
     *
     * @param view
     */
    private void initViews(View view) {

        listFolders = (SwipeMenuListView) view.findViewById(R.id.listFolders);
        fileHelpers = new FileHelpers();
        database = new DataBaseHelper(getActivity());

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity());
                // set item background

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
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

        listFolders.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, final int index) {
                switch (index) {
                    case 0:
                        if (position == 0) {
                            Toast.makeText(getActivity(), "Cannot Delete \"Default\" folder", Toast.LENGTH_LONG).show();
                        } else {
                            new AlertDialog.Builder(getActivity()).setTitle("Delete Folder")
                                    .setMessage("Do you really want to delete \"" + foldersArray[position].getName() + "\" folder")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //  Toast.makeText(getActivity(),"positon"+ position+" index" +index,Toast.LENGTH_LONG).show();
                                            fileHelpers.deleteFolder(foldersArray[position].getName());
                                            database.deleteRecordFolder(foldersArray[position].getName());
                                            new FetchFolders().execute();
                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        }

                        break;

                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        listFolders.setMenuCreator(creator);


        listFolders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), RecordingActivity.class);
                intent.putExtra("TITLE", foldersArray[i].getName());
                startActivity(intent);

            }
        });
        new FetchFolders().execute();
    }


    public static int dp2px(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


    public void createFolder(String folderName) {
        fileHelpers.makeNewFolder(folderName);
        new FetchFolders().execute();

    }

    private class FetchFolders extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDailog = new ProgressDialog(getActivity());
            pDailog.setMessage("Loading...");
            pDailog.setCancelable(false);
            pDailog.show();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            foldersArray = null;
            foldersArray = fileHelpers.fetchAllFolders();
            adapterFolders = new AdapterFolders(getActivity(), foldersArray);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDailog.dismiss();
            listFolders.setAdapter(adapterFolders);
        }
    }

}
