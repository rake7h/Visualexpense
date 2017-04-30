package rakesh.visualexpense;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import rakesh.visualexpense.NavigationDrawer.NavDrawerActivity;

/**
 * Created by rakesh on 31/03/16.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Intent intent = new Intent(this, MainActivity.class);
        Intent intent = new Intent(this, NavDrawerActivity.class);
        startActivity(intent);
        finish();
    }
}