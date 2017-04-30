package rakesh.visualexpense.Onboarding;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import rakesh.visualexpense.helper.SessionManager;
import rakesh.visualexpense.NavigationDrawer.NavDrawerActivity;
import rakesh.visualexpense.R;

public class MainActivity extends AppCompatActivity {
    private SessionManager session;
    public static final String PREF_USER_FIRST_TIME = "user_first_time";
    boolean isUserFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Session manager
        session = new SessionManager(getApplicationContext());
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main_listview activity
            Intent intent = new Intent(this, NavDrawerActivity.class);
            startActivity(intent);
            finish();
        }
          isUserFirstTime = Boolean.valueOf(Utils.readSharedSetting(MainActivity.this, PREF_USER_FIRST_TIME, "true"));

        Intent introIntent = new Intent(MainActivity.this, PagerActivity.class);
        introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);


        if (isUserFirstTime) {

            startActivity(introIntent);
        }

        setContentView(R.layout.activity_main2);

    }


}
