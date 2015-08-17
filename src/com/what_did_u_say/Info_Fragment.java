package com.what_did_u_say;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
