package rakesh.visualexpense.NavigationDrawer;

import android.app.Activity;
import android.app.Fragment;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rakesh.visualexpense.AnalyticsTrackers;
import rakesh.visualexpense.DatabaseHelper.DatabaseHelper;

import rakesh.visualexpense.R;
import rakesh.visualexpense.module.Item;

public class CircleFragment extends ListFragment {


    public CircleFragment() {
    }

    private static CircleFragment mInstance;
    public DatabaseHelper databaseHelper;
    private ProgressBar progBar;
    private TextView pertext;
    public TextView inwallet;
    public TextView income;
    private TextView noexpense_txt;
    public TextView expenses;
    public ListView listView;
    public ListViewAdapter2 adapter;
    public List<Item> itemList;
    private Handler mHandler = new Handler();
    private int mProgressStatus = 0;
    public static String CurrentExpense;

    FrameLayout fab;
    ImageButton fabBtn;
    View fabShadow;
//---------------------------------------//////////-------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        AnalyticsTrackers.initialize(getActivity());
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);



        View rootView = inflater.inflate(R.layout.activity_circle_main, container, false);

        databaseHelper = new DatabaseHelper(getActivity());

        progBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        pertext = (TextView) rootView.findViewById(R.id.TXT1);
        income = (TextView) rootView.findViewById(R.id.totalview);
        inwallet = (TextView) rootView.findViewById(R.id.leftview);
        expenses = (TextView) rootView.findViewById(R.id.spendview);
        listView = (ListView) rootView.findViewById(android.R.id.list);
        noexpense_txt=(TextView)rootView.findViewById(R.id.no_expense);
        noexpense_txt.setVisibility(getView().INVISIBLE);

        itemList = new ArrayList<>();
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
                addingNewItemDialog();

            }
        });



        reloadingDatabase1();


        return rootView;

    }

    public void DrawCircle() {

        new Thread(new Runnable() {
            public void run() {

                final int presentage = databaseHelper.getpercent();
                while (mProgressStatus < presentage) {
                    mProgressStatus += 1;
                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            progBar.setProgress(mProgressStatus);
                            pertext.setText(+mProgressStatus + "%");


                        }
                    });
                    try {


                        Thread.sleep(4);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void addingNewItemDialog() {
        //DialogFragment alertDialog = new DialogFragment();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("New Expense");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setPadding(10, 10, 10, 10);
        layout.setOrientation(LinearLayout.VERTICAL);


        final EditText nameBox = new EditText(getActivity());
        nameBox.setHint("Name");
        nameBox.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        nameBox.setInputType(InputType.TYPE_CLASS_TEXT);
        nameBox.setMaxLines(1);
        layout.addView(nameBox);


        final EditText pricebox = new EditText(getActivity());
        pricebox.setHint("Price");


        pricebox.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});

        pricebox.setMaxLines(1);
        pricebox.setInputType(InputType.TYPE_CLASS_NUMBER);


        layout.addView(pricebox);

        final TextView catview = new TextView(getActivity());
        catview.setText("Category");
        layout.addView(catview);


        final Spinner mSpinner = new Spinner(getActivity());
        mSpinner.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));

        String[] s = {"Food ", "Travel", "Recharge ", "Fun", "Eating Out", "Shopping", "Medical", "Transport", "Other"};

        final ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, s);



        mSpinner.setAdapter(adp);
        layout.addView(mSpinner);


        final EditText desbox = new EditText(getActivity());
        desbox.setHint("Note...");
        desbox.setFilters(new InputFilter[]{new InputFilter.LengthFilter(255)});
        desbox.setMaxLines(3);

        desbox.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(desbox);
        final String[] catitem = new String[1];

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String catitems = mSpinner.getSelectedItem().toString();
                catitem[0] =catitems;
                Log.i("Selected item : ", catitems);

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }

        });



        alertDialog.setView(layout);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

//LATEST EXPENSE ENTRY
                String price = pricebox.getText().toString();
                String name = nameBox.getText().toString();

                float saving;
                saving = databaseHelper.getRemainWallet();
                if (price.isEmpty()) {
                    //Toast.makeText(getActivity(), "plz enter Amount ", Toast.LENGTH_SHORT).show();
                    pricebox.setError("Enter Amount");


                } else if (name.equals("")) {
                    nameBox.setError("Enter expense name");

                } else if (Integer.parseInt(price) > saving) {
                    Snackbar.make(listView, "You have only   \u20B9 " +saving, Snackbar.LENGTH_LONG).show();


                } else {
                    int selectionPosition = adp.getPosition("");
                    mSpinner.setSelection(selectionPosition);


                    Item item = new Item(getText(nameBox), getText(pricebox), catitem[0], getText(desbox));
                    databaseHelper.addNewItem(item);

                    CurrentExpense = getText(pricebox);
                    Fragment fragment = null;

                    fragment = new CircleFragment();


                    if (fragment != null) {
                        FragmentManager fragmentManager = getFragmentManager();

                        fragmentManager.beginTransaction().replace(R.id.nav_contentframe, fragment).commit();

                    }
                }
                //databaseHelper.latestExpense(CurrentExpense);

                //reloadingDatabase(); //reload the db to view
            }
        });

        alertDialog.setNegativeButton("Cancel", null);

        //show alert
        alertDialog.show();
    }


    private String getText(TextView textView) {
        return textView.getText().toString().trim();
    }

    public void reloadingDatabase1() {
        income.setText(""+ databaseHelper.getSumWallet());
        expenses.setText(""+ databaseHelper.getSumExpense());
        inwallet.setText(""+ databaseHelper.getRemainWallet());
        itemList = databaseHelper.Getlast4();
        if (itemList.size() == 0) {
            //Toast.makeText(getActivity(), "No Expenses found", Toast.LENGTH_SHORT).show();
            //Snackbar.make(listView, "No Expenses found", Snackbar.LENGTH_LONG).show();

            noexpense_txt.setVisibility(getView().VISIBLE);
    }

        adapter = new ListViewAdapter2(getActivity(), R.layout.item_listview_design, itemList, databaseHelper);
        listView.setAdapter(adapter);

        DrawCircle();

    }

    private class ListViewAdapter2 extends ArrayAdapter<Item> {

        public CircleFragment fragment;

        private DatabaseHelper databaseHelper;
        private List<Item> ItemList;


        public ListViewAdapter2(Activity context, int resource, List<Item> objects, DatabaseHelper helper) {
            super(getActivity(), resource, objects);
            this.databaseHelper = helper;
            this.ItemList = objects;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_listview_design, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.itemname.setText(getItem(position).getItemName());
            holder.itemprice.setText(getItem(position).getItemprice());


//Delete an item
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseHelper.deleteItem(getItem(position)); //delete in db
                    //Toast.makeText(getActivity(), "Deleted!", Toast.LENGTH_SHORT).show();

                    Snackbar.make(listView, "One expense deleted", Snackbar.LENGTH_SHORT).show();
                    //reload the database to view

                    fragment = new CircleFragment();
                    FragmentManager fragmentManager2 = getFragmentManager();
                    fragmentManager2.beginTransaction().replace(R.id.nav_contentframe, fragment).commit();
                }
            });

            //Edit/Update an item
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                    alertDialog.setTitle("Edit expense");

                    LinearLayout layout = new LinearLayout(getActivity());
                    layout.setPadding(10, 10, 10, 10);
                    layout.setOrientation(LinearLayout.VERTICAL);


                    final EditText nameBox = new EditText(getActivity());
                    nameBox.setHint("Name");
                    layout.addView(nameBox);


                    final EditText pricebox = new EditText(getActivity());
                    pricebox.setHint("Price");
                    pricebox.setInputType(InputType.TYPE_CLASS_NUMBER);
                    layout.addView(pricebox);

                    final TextView catview =new TextView(getActivity());
                    catview.setText("Category");
                    layout.addView(catview);


                    final Spinner mSpinner = new Spinner(getActivity());
                    mSpinner.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));

                    String[] s = {"Food ", "Travel", "Recharge ", "Fun", "Eating Out", "Shopping", "Medical", "Transport", "Other"};
                    final ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_item, s);

                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            Object item = parent.getItemAtPosition(pos);

                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                        }

                    });

                    mSpinner.setAdapter(adp);
                    layout.addView(mSpinner);

                    final EditText noteBox = new EditText(getActivity());
                    nameBox.setHint("Note");
                    layout.addView(noteBox);


                    final String itemText = (String) mSpinner.getItemAtPosition(mSpinner.getSelectedItemPosition()).toString();

                    nameBox.setText(getItem(position).getItemName());
                    pricebox.setText(getItem(position).getItemprice());

                    int selectionPosition= adp.getPosition("");
                    mSpinner.setSelection(selectionPosition);

                    alertDialog.setView(layout);
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String price = pricebox.getText().toString();
                                    String name = nameBox.getText().toString();

                                    float saving;
                                    saving = databaseHelper.getRemainWallet();
                                    if (price.isEmpty()) {
                                        //Toast.makeText(getActivity(), "plz enter Amount ", Toast.LENGTH_SHORT).show();
                                        pricebox.setError("Enter Amount");


                                    } else if (name.equals("")) {
                                        nameBox.setError("Enter expense name");

                                    } else if (Integer.parseInt(price) > saving) {
                                        Snackbar.make(listView, "You have only   \u20B9 " + saving, Snackbar.LENGTH_LONG).show();


                                    } else {

                                        int selectionPosition = adp.getPosition("");
                                        mSpinner.setSelection(selectionPosition);
//update on click
                                        Item item = new Item(nameBox.getText().toString(), pricebox.getText().toString(), "",noteBox.getText().toString());

                                        item.setId(getItem(position).getId());
                                        databaseHelper.updateItem(item); //update to db
                                        //Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();
                                        Snackbar.make(listView, "Expense updated", Snackbar.LENGTH_SHORT).show();
                                        //reload the database to view
                                        reloadingDatabase1();

                                    }
                                }

                            }

                    );

                    alertDialog.setNegativeButton("Cancel",null);

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

                    TextView nameBox = new TextView(getActivity());
                    layout.addView(nameBox);

                    TextView jobBox = new TextView(getActivity());
                    layout.addView(jobBox);

                    TextView dateBox = new TextView(getActivity());
                    layout.addView(dateBox);

                    TextView catBox = new TextView(getActivity());
                    layout.addView(catBox);

                    TextView notebox = new TextView(getActivity());
                    layout.addView(notebox);

                    nameBox.setText("NAME:            " + getItem(position).getItemName());
                    jobBox.setText("AMOUNT:      " + getItem(position).getItemprice());
                    dateBox.setText("TIME:              " + getItem(position).getItemCat());
                    catBox.setText("CATEGORY:   " + getItem(position).setItemdate());
                    notebox.setText("NOTE:    " + getItem(position).getItemnote());
                    alertDialog.setView(layout);
                    alertDialog.setNegativeButton("OK", null);


                    //show alert
                    alertDialog.show();
                }
            });

            return convertView;
        }

        private class ViewHolder {
            private TextView itemname;
            private TextView itemprice;
            private View btnDelete;
            private View btnEdit;

            public ViewHolder(View v) {
                itemname = (TextView) v.findViewById(R.id.amount_view);
                itemprice = (TextView) v.findViewById(R.id.amount_date);

                btnDelete = v.findViewById(R.id.delete);
                btnEdit = v.findViewById(R.id.edit);
            }
        }


    }




    //Analytics Code____________________________________________________________


}