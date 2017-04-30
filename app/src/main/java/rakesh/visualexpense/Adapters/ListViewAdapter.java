package rakesh.visualexpense.Adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import rakesh.visualexpense.Activity.ViewListActivity_main;
import rakesh.visualexpense.DatabaseHelper.DatabaseHelper;
import rakesh.visualexpense.R;
import rakesh.visualexpense.module.Item;

public class ListViewAdapter extends ArrayAdapter<Item> {

    private ViewListActivity_main activity;

    private DatabaseHelper databaseHelper;
    private List<Item> ItemList;

    public ListViewAdapter(ViewListActivity_main context, int resource, List<Item> objects, DatabaseHelper helper) {
        super(context, resource, objects);
        this.activity = context;
        this.databaseHelper = helper;
        this.ItemList = objects;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

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
                Toast.makeText(activity, "Deleted!", Toast.LENGTH_SHORT).show();

                //reload the database to view
                activity.reloadingDatabase();
            }
        });
        //Edit/Update an item
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                alertDialog.setTitle("Edit expense");

                LinearLayout layout = new LinearLayout(activity);
                layout.setPadding(10, 10, 10, 10);
                layout.setOrientation(LinearLayout.VERTICAL);


                final EditText nameBox = new EditText(activity);
                nameBox.setHint("Name");
                layout.addView(nameBox);


                final EditText pricebox = new EditText(activity);
                pricebox.setHint("Price");
                pricebox.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(pricebox);

                final TextView catview =new TextView(activity);
                catview.setText("Category");
                layout.addView(catview);


                final Spinner mSpinner = new Spinner(activity);
                mSpinner.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));

                String[] s = {"Food ", "Travel", "Recharge ", "Fun", "Eating Out", "Shopping", "Medical", "Transport", "Other"};
                final ArrayAdapter<String> adp = new ArrayAdapter<String>(activity,
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
                                    Snackbar.make(view, "You have only   \u20B9 " + saving, Snackbar.LENGTH_LONG).show();


                                } else {

                                    //int selectionPosition = adp.getPosition("");
                                    //mSpinner.setSelection(selectionPosition);
//update on click
                                    Item item = new Item(nameBox.getText().toString(), pricebox.getText().toString(), mSpinner.getSelectedItem().toString(),pricebox.getText().toString());

                                    item.setId(getItem(position).getId());
                                    databaseHelper.updateItem(item); //update to db
                                    //Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();
                                    Snackbar.make(view, "Expense updated", Snackbar.LENGTH_SHORT).show();
                                    //reload the database to view
                                    activity.reloadingDatabase();

                                }
                            }

                        }

                );

                alertDialog.setNegativeButton("Cancel", null);

                //show alert
                alertDialog.show();
            }
        });


//show details when each row item clicked
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Details");

                LinearLayout layout = new LinearLayout(activity);
                layout.setPadding(10, 10, 10, 10);
                layout.setOrientation(LinearLayout.VERTICAL);

                TextView nameBox = new TextView(activity);
                layout.addView(nameBox);

                TextView jobBox = new TextView(activity);
                layout.addView(jobBox);

                TextView dateBox = new TextView(activity);
                layout.addView(dateBox);

                TextView catBox = new TextView(activity);
                layout.addView(catBox);

                TextView desBox = new TextView(activity);
                layout.addView(desBox);

                nameBox.setText("NAME:            " + getItem(position).getItemName());
                 jobBox.setText("AMOUNT:      " + getItem(position).getItemprice());
                dateBox.setText("TIME:              " + getItem(position).getItemCat());
                 catBox.setText("CATEGORY:   " + getItem(position).setItemdate());
                desBox.setText("NOTE:   " + getItem(position).getItemnote());

                alertDialog.setView(layout);
                alertDialog.setNegativeButton("OK", null);


                //show alert
                alertDialog.show();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        private TextView itemname;
        private TextView itemprice;
        private View btnDelete;
        private View btnEdit;

        public ViewHolder (View v) {
            itemname = (TextView)v.findViewById(R.id.amount_view);
            itemprice = (TextView)v.findViewById(R.id.amount_date);

            btnDelete = v.findViewById(R.id.delete);
            btnEdit = v.findViewById(R.id.edit);
        }
    }


}