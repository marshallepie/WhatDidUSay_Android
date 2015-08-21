package com.example.root.whatdidusay;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by root on 21/8/15.
 */
public class Info_Fragment extends Fragment {

    private static View view;
    static Activity act;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.info_fragment, container, false);
        act = getActivity();

        return view;

    }


}
