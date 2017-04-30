package rakesh.visualexpense.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rakesh.visualexpense.DatabaseHelper.DatabaseHelper;
import rakesh.visualexpense.R;
import rakesh.visualexpense.module.Item;

public class OneFragment extends Fragment{

    public OneFragment() {
        // Required empty public constructor
    }

    public DatabaseHelper databaseHelper;
    public ListView listView;
    public ListViewAdapter2 adapter;
    public List<Item> itemList;
    public TextView totalexpense;
    private TextView noexpense_txt;
    public String latestexpense;
    private TextView Todayexpense;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_one, container, false);


        databaseHelper = new DatabaseHelper(getActivity());
        listView = (ListView) rootView.findViewById(android.R.id.list);
        totalexpense = (TextView) rootView.findViewById(R.id.totalexpense_view1);
        Todayexpense = (TextView) rootView.findViewById(R.id.todayexpenseview1);
        noexpense_txt = (TextView) rootView.findViewById(R.id.no_expense1);
        noexpense_txt.setVisibility(getView().INVISIBLE);
        itemList = new ArrayList<>();


        reloadingDatabase(); //loading table of DB to ListView

        // Inflate the layout for this fragment
        return rootView;
    }

    public void reloadingDatabase() {
        itemList = databaseHelper.getAllItems();
        if (itemList.size() == 0) {

            noexpense_txt.setVisibility(getView().VISIBLE);
            totalexpense.setVisibility(getView().INVISIBLE);
            Todayexpense.setVisibility(getView().INVISIBLE);

        }

        else {
            adapter = new ListViewAdapter2(getActivity(), R.layout.item_listview_design, itemList, databaseHelper);
            listView.setAdapter(adapter);
            totalexpense.setVisibility(getView().VISIBLE);
            totalexpense.setText("Total Expense: " + databaseHelper.getSumExpense());
            Todayexpense.setVisibility(View.VISIBLE);
            Todayexpense.setText("Saving: " + databaseHelper.getRemainWallet());
        }
    }
    private class ListViewAdapter2 extends ArrayAdapter<Item> {

        public OneFragment fragment;

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
                    reloadingDatabase();


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
                                        reloadingDatabase();

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
}