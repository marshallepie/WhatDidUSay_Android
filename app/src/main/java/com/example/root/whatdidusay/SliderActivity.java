package com.example.root.whatdidusay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.ArrayList;
import java.util.List;


public class SliderActivity extends SlidingFragmentActivity {

    String[] titlearr = {"Home", "Info", "Settings"};

    int[] icons = {R.drawable.home, R.drawable.info, R.drawable.settings};
    static FragmentActivity act;
    RelativeLayout menu;
    ListView listviewMenu;

    public static ImageView slider_btn;

    public static TextView wdys_text;

    public static ImageView edit_btn;

    private static FragmentManager fm;
    private static FragmentTransaction ft;
    Typeface avalon_regular, avalon_bold;

    public static List<myFragmentsclass> myFragents = new ArrayList<myFragmentsclass>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.activity_menu);

        getSlidingMenu().setBehindOffset(120);

        act = SliderActivity.this;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();

            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
        }

       /* avalon_regular = Typeface.createFromAsset(act.getAssets(),
                "avalon-plain.ttf");

        avalon_bold = Typeface.createFromAsset(act.getAssets(),
                "Avalon Bold.ttf");*/

        slider_btn = (ImageView) findViewById(R.id.slider_btn);
        wdys_text = (TextView) findViewById(R.id.wdys_text);
        //  wdys_text.setTypeface(avalon_bold);
        edit_btn = (ImageView) findViewById(R.id.edit_btn);

        slider_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                showMenu();

            }
        });

        wdys_text.setText("What Did U Say");

        menu = (RelativeLayout) findViewById(R.id.menubar);

        listviewMenu = (ListView) menu.findViewById(R.id.listviewMenu);
        listviewMenu.setAdapter(new MenuAdapter(this));

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        Fragment fragment = new Home_Fragment();
        ft.add(R.id.activity_main_content_fragment, fragment);
        ft.commit();

        myFragmentsclass p = new myFragmentsclass();
        p.fragment = fragment;
        p.name = "What Did U Say";
        myFragents.add(p);

        listviewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressLint("Recycle")
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                edit_btn.setVisibility(View.VISIBLE);

                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter, R.anim.exit);
                Fragment fragment = null;

                myFragents.clear();

                String selectedItem = titlearr[arg2];

                if (selectedItem.compareTo("Home") == 0) {
                    fragment = new Home_Fragment();
                    wdys_text.setText("What Did U Say");

                }
                if (selectedItem.compareTo("Info") == 0) {
                    fragment = new Info_Fragment();
                    edit_btn.setVisibility(View.INVISIBLE);
                    wdys_text.setText("Info");
                }
                if (selectedItem.compareTo("Settings") == 0) {
                    fragment = new Settings_Fragment();
                    edit_btn.setVisibility(View.INVISIBLE);
                    wdys_text.setText("Settings");
                }

                if (wdys_text.getText().toString()
                        .equalsIgnoreCase(titlearr[arg2])) {

                }
                if (fragment != null) {

                    // wdys_text.setText(titlearr[arg2]);
                    ft.add(R.id.activity_main_content_fragment, fragment);
                    ft.commit();

                    myFragmentsclass p = new myFragmentsclass();
                    p.fragment = fragment;
                    p.name = titlearr[arg2];
                    myFragents.add(p);

                }

                showContent();

            }
        });

    }

    private class MenuAdapter extends BaseAdapter {
        private LayoutInflater inflater = null;
        private View previousView;

        Context con;

        public MenuAdapter(Context c) {
            inflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return titlearr.length;
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
            TextView name;
            ImageView iv_iconmenu;

        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View v = convertView;

            final SettingHolder holder;
            holder = new SettingHolder();

            v = inflater.inflate(R.layout.cust_menulist, null);

            holder.name = (TextView) v.findViewById(R.id.listText);
            holder.iv_iconmenu = (ImageView) v.findViewById(R.id.iv_iconmenu);

            holder.name.setText(titlearr[position]);

            holder.iv_iconmenu.setBackgroundResource(icons[position]);

            return v;
        }

    }

    public static class myFragmentsclass {
        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        Fragment fragment;
        String name;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
