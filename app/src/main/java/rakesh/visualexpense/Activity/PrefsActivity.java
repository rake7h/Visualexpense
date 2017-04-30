package rakesh.visualexpense.Activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import rakesh.visualexpense.R;

/**
 * Created by rakesh on 28/03/16.
 */
public class PrefsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
