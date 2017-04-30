package rakesh.visualexpense.NavigationDrawer;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rakesh.visualexpense.R;
import rakesh.visualexpense.Wallet.Wallet_fill_Fragment;

public class Empty_wallet extends Fragment {



    public Empty_wallet(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_empty_wallet, container, false);
        TextView clicktxt = (TextView) rootView.findViewById(R.id.empty_wallet_view2);


        clicktxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewIn) {

                Fragment fragment = new Wallet_fill_Fragment();

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_contentframe, fragment).commit();

            }
        });


        return rootView;
    }


}