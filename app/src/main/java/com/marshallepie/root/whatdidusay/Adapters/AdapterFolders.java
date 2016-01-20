package com.marshallepie.root.whatdidusay.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.marshallepie.root.whatdidusay.R;

import java.io.File;

/**
 * Created by dottechnologies on 6/1/16.
 */
public class AdapterFolders extends BaseAdapter {

    private File[] mlistFolders;
    private LayoutInflater mInflater = null;
    private Context mContext;

    public AdapterFolders(Context context, File[] list) {
        mlistFolders = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return mlistFolders.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;


        if (view == null) {

            view = mInflater.inflate(R.layout.item_folder_list, null);
            holder = new ViewHolder();
            holder.nameFolder = (TextView) view.findViewById(R.id.textFolder);

            view.setTag(holder);

        } else {

            holder = (ViewHolder) view.getTag();

        }

        holder.nameFolder.setText(mlistFolders[i].getName());


        return view;
    }

    class ViewHolder {

        private TextView nameFolder;
    }


}
