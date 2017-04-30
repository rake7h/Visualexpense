package rakesh.visualexpense.Wallet;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rakesh.visualexpense.DatabaseHelper.DatabaseHelper;
import rakesh.visualexpense.R;

import rakesh.visualexpense.module.Wallet;

public class Wallet_Records_fragment extends Fragment {


    public Wallet_Records_fragment(){}

    public ListView listView;
    public ListViewAdapter2 adapter;
    public List<Wallet> itemList;
    private Handler mHandler = new Handler();

    FrameLayout fab;
    ImageButton fabBtn;
    View fabShadow;
    private Fragment fragment = null;

    public DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_wallet_records, container, false);

        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.simple_grow);

        fab = (FrameLayout) rootView.findViewById(R.id.myfab_main);
        fabBtn = (ImageButton) rootView.findViewById(R.id.myfab_main_btn);
        fabShadow = rootView.findViewById(R.id.myfab_shadow);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fabShadow.setVisibility(View.GONE);
            fabBtn.setBackground(getActivity().getDrawable(R.drawable.ripple_accent));
        }

        fab.startAnimation(animation);

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICK", "FAB CLICK");

                fragment = new Wallet_fill_Fragment();


                FragmentManager fragmentManager = getFragmentManager();

                fragmentManager.beginTransaction().replace(R.id.nav_contentframe, fragment).commit();




            }
        });


        databaseHelper = new DatabaseHelper(getActivity());

        listView = (ListView) rootView.findViewById(android.R.id.list);
        itemList = new ArrayList<>();
        reloadingDatabase();

        return rootView;
    }




    private class ListViewAdapter2 extends ArrayAdapter<Wallet> {

        public Wallet_Records_fragment fragment;

        private DatabaseHelper databaseHelper;
        private List<Wallet> ItemList;


        public ListViewAdapter2(Activity context, int resource, List<Wallet> objects, DatabaseHelper helper) {
            super(getActivity(), resource, objects);
            this.databaseHelper = helper;
            this.ItemList = objects;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.wallet_listview_design, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.wallet_amount.setText(getItem(position).getAmount());
            holder.wallet_date.setText(getItem(position).setDate());
            holder.wallet_sno.setText(Integer.toString(getItem(position).getId()));

            //Delete an item
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseHelper.deleteincome(getItem(position)); //delete in db
                    Toast.makeText(getActivity(), "Deleted!", Toast.LENGTH_SHORT).show();

                    //reload the database to view
                    reloadingDatabase();
                }
            });
            //Edit/Update an item
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Update a Wallet");

                    LinearLayout layout = new LinearLayout(getActivity());
                    layout.setPadding(10, 10, 10, 10);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    final EditText Amount_box = new EditText(getActivity());
                    Amount_box.setHint("Amount");
                    layout.addView(Amount_box);

                    final EditText Note_box = new EditText(getActivity());
                    Note_box.setHint("Note");
                    layout.addView(Note_box);

                    final EditText catBox = new EditText(getActivity());
                    //catBox.setHint("Price");
                    layout.addView(catBox);

                    Amount_box.setText(getItem(position).getAmount());
                    Note_box.setText(getItem(position).getNote());


                    alertDialog.setView(layout);

                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Wallet wallet = new Wallet(Amount_box.getText().toString(), Note_box.getText().toString());
                            wallet.setid(getItem(position).getId());
                            databaseHelper.updateIncome(wallet); //update to db
                            Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();

                            //reload the database to view
                            reloadingDatabase();
                        }
                    });

                    alertDialog.setNegativeButton("Cancel", null);

                    //show alert
                    alertDialog.show();
                }
            });

//show details when each row item clicked
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Details");

                    LinearLayout layout = new LinearLayout(getActivity());
                    layout.setPadding(10, 10, 10, 10);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    TextView Amountbox1 = new TextView(getActivity());
                    layout.addView(Amountbox1);

                    TextView DateBox = new TextView(getActivity());
                    layout.addView(DateBox);

                    TextView NoteBox = new TextView(getActivity());
                    layout.addView(NoteBox);



                    Amountbox1.setText("AMOUNT:            " + getItem(position).getAmount());
                    DateBox.setText("TIME:      " + getItem(position).setDate());
                    NoteBox.setText("NOTE:              " + getItem(position).getNote());

                    alertDialog.setView(layout);
                    alertDialog.setNegativeButton("OK", null);


                    //show alert
                    alertDialog.show();
                }
            });




            return convertView;
        }

        private class ViewHolder {
            private TextView wallet_amount;
            private TextView wallet_date;
            private TextView wallet_sno;
            private View btnDelete;
            private View btnEdit;


            public ViewHolder(View v) {
                wallet_amount = (TextView) v.findViewById(R.id.amount_view);
                wallet_date = (TextView) v.findViewById(R.id.amount_date);
                wallet_sno = (TextView) v.findViewById(R.id.sno);
                btnDelete = v.findViewById(R.id.delete);
                btnEdit = v.findViewById(R.id.edit);

            }
        }


    }
    public void reloadingDatabase() {


        itemList = databaseHelper.getAllincome();
        if (itemList.size() == 0) {
            Toast.makeText(getActivity(), "Wallet is empty!", Toast.LENGTH_SHORT).show();
        }

        adapter = new ListViewAdapter2(getActivity(), R.layout.wallet_listview_design, itemList, databaseHelper);
        listView.setAdapter(adapter);
    }

}