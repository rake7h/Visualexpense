package rakesh.visualexpense.Wallet;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import rakesh.visualexpense.DatabaseHelper.DatabaseHelper;
import rakesh.visualexpense.NavigationDrawer.CircleFragment;
import rakesh.visualexpense.R;
import rakesh.visualexpense.module.Wallet;

public class Wallet_fill_Fragment extends Fragment {


    public Wallet_fill_Fragment(){}

    public EditText AmountBox;
    public Button bttnSave;
    InterstitialAd mInterstitialAd;
    private AdView mAdView;

    public DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_wallet_fill, container, false);


        //adv view---------------------

        NativeExpressAdView adView = (NativeExpressAdView)rootView.findViewById(R.id.adView1);
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);
        //adv view---------------------

        databaseHelper = new DatabaseHelper(getActivity());

        AmountBox = (EditText) rootView.findViewById(R.id.amount_wallet_fragment);
        bttnSave = (Button) rootView.findViewById(R.id.fill_wallet_fragment);



        fillWallet();

        return rootView;
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

    }

    public void fillWallet(){
    bttnSave.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Wallet wallet = new Wallet(getText(AmountBox), null);

                    Boolean isInserted = databaseHelper.addWallet(wallet);

                    if (isInserted = true) {
                        Snackbar.make(getView(), "Wallet updated", Snackbar.LENGTH_SHORT).show();

                        Fragment fragment = new CircleFragment();


                        FragmentManager fragmentManager = getFragmentManager();

                        fragmentManager.beginTransaction().replace(R.id.nav_contentframe, fragment).commit();

                    }
                    else

                        Toast.makeText(getActivity(), "Data Not Inserted", Toast.LENGTH_LONG).show();


                }

                private String getText(EditText amountBox)

                {


                    return amountBox.getText().toString().trim();
                }
            }
    );

}

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


}