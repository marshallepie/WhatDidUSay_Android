package com.example.root.whatdidusay;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by root on 21/8/15.
 */
public class
        Info_Fragment extends Fragment {

    private static View view;
    static Activity act;
    private WebView webView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.info_fragment, container, false);
        act = getActivity();
        webView = (WebView) view.findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/info.html");

        return view;

    }


}
