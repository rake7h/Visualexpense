package rakesh.visualexpense.NavigationDrawer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;


import rakesh.visualexpense.Activity.Main_tab;
import rakesh.visualexpense.Activity.ViewListActivity_main;
import rakesh.visualexpense.DatabaseHelper.DatabaseHelper;
import rakesh.visualexpense.R;
import rakesh.visualexpense.Wallet.Wallet_Records_fragment;

public class NavDrawerActivity extends AppCompatActivity {

    private static final String PREFERENCES_FILE = "mymaterialapp_settings";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    NavigationView mNavigationView;
    FrameLayout mContentFrame;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private int mCurrentSelectedPosition;

    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;
    private String[] navMenuTitles;
    private Fragment fragment = null;
    private DatabaseHelper databaseHelper;


    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(this);

        setContentView(R.layout.activity_nav_drawer);
        setUpToolbar();

        int incomeAmount = databaseHelper.getSumWallet();

        if (incomeAmount == 0) {

            fragment = new Empty_wallet();

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_contentframe, fragment).commit();

        }
        else {


            fragment = new CircleFragment();


            FragmentManager fragmentManager = getFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.nav_contentframe, fragment).commit();

        }



        mTitle = mDrawerTitle = getTitle();
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);

        mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(this, PREF_USER_LEARNED_DRAWER, "false"));

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        setUpNavDrawer();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mContentFrame = (FrameLayout) findViewById(R.id.nav_contentframe);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                Fragment fragment = null;

                menuItem.setChecked(true);
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_item_1://dashboard

                            fragment = new CircleFragment();
                            if (fragment != null) {
                            FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.nav_contentframe, fragment).commit();
                                mCurrentSelectedPosition = 0;

                                setTitle(navMenuTitles[mCurrentSelectedPosition]);
                            mDrawerLayout.closeDrawer(mNavigationView);
                            }
                            return true;

                    case R.id.navigation_item_2: //item list

                        Intent intent = new Intent(NavDrawerActivity.this, Main_tab.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(mNavigationView);
                        return true;

                    case R.id.navigation_item_3: //wallet

                        fragment = new Wallet_Records_fragment();


                        if (fragment != null) {
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.nav_contentframe, fragment).commit();
                            mCurrentSelectedPosition = 2;

                            setTitle(navMenuTitles[mCurrentSelectedPosition]);
                            mDrawerLayout.closeDrawer(mNavigationView);

                        }
                        return true;


                    case R.id.navigation_item_5:
                        mCurrentSelectedPosition = 4;
                        setTitle(navMenuTitles[mCurrentSelectedPosition]);
                        mDrawerLayout.closeDrawer(mNavigationView);
                        Snackbar.make(mContentFrame, "Setting", Snackbar.LENGTH_SHORT).show();
                        return true;

                    case R.id.navigation_item_6:
                        mCurrentSelectedPosition = 5;

                        setTitle(navMenuTitles[mCurrentSelectedPosition]);
                        mDrawerLayout.closeDrawer(mNavigationView);
                        Snackbar.make(mContentFrame, "About", Snackbar.LENGTH_SHORT).show();

                        return true;


                    default:


                        return true;


                }

            }

        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, 0);
        Menu menu = mNavigationView.getMenu();
        menu.getItem(mCurrentSelectedPosition).setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:
                return true;

            case R.id.action_help:
                Uri uri = Uri.parse("http://www.visualdeveloper.in"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;

            case R.id.view_expense:

                Intent intent2 = new Intent(getApplicationContext(), Main_tab.class);
                startActivity(intent2);
                return true;

            case R.id.Add_wallet:

                fragment = new Wallet_Records_fragment();


                FragmentManager fragmentManager = getFragmentManager();

                fragmentManager.beginTransaction().replace(R.id.nav_contentframe, fragment).commit();
                setTitle(navMenuTitles[mCurrentSelectedPosition]);

                return true;

            case R.id.view_pie:

                fragment = new PieFragment();


                FragmentManager fragmentManager2 = getFragmentManager();

                fragmentManager2.beginTransaction().replace(R.id.nav_contentframe, fragment).commit();
                setTitle(navMenuTitles[mCurrentSelectedPosition]);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_list_main);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_menu);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        //if (!mUserLearnedDrawer) {
        //  mDrawerLayout.openDrawer(GravityCompat.START);
        //mUserLearnedDrawer = true;
        ///saveSharedSetting(this, PREF_USER_LEARNED_DRAWER, "true");
        //}

    }
}

