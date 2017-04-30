package rakesh.visualexpense.Activity;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rakesh.visualexpense.DatabaseHelper.DatabaseHelper;
import rakesh.visualexpense.module.Item;
import rakesh.visualexpense.Adapters.ListViewAdapter;
import rakesh.visualexpense.R;

public class ViewListActivity_main extends AppCompatActivity {

    public DatabaseHelper databaseHelper;
    public ListView listView;
    public ListViewAdapter adapter;
    public List<Item> itemList;
    public TextView totalexpense;
    private TextView noexpense_txt;
    public String latestexpense;
    private TextView Todayexpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listView = (ListView) findViewById(R.id.list_view_tab);
        totalexpense = (TextView) findViewById(R.id.totalexpense_view);
        Todayexpense = (TextView) findViewById(R.id.todayexpenseview1);

        databaseHelper = new DatabaseHelper(this);
        noexpense_txt = (TextView) findViewById(R.id.no_expense);
        noexpense_txt.setVisibility(View.INVISIBLE);

        itemList = new ArrayList<>();


        reloadingDatabase(); //loading table of DB to ListView

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                // go to previous activity
                onBackPressed();

                return true;


            case R.id.action_settings:


                return true;
            case R.id.view_expense:

                Intent intent = new Intent(getApplicationContext(), ViewListActivity_main.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    public void reloadingDatabase() {
        itemList = databaseHelper.getAllItems();
        if (itemList.size() == 0) {

            noexpense_txt.setVisibility(View.VISIBLE);

            totalexpense.setVisibility(View.INVISIBLE);
            Todayexpense.setVisibility(View.INVISIBLE);

        }
        adapter = new ListViewAdapter(this, R.layout.item_listview_design, itemList, databaseHelper);
        listView.setAdapter(adapter);
        totalexpense.setVisibility(View.VISIBLE);
        totalexpense.setText("Total Expense: " + databaseHelper.getSumExpense());
        Todayexpense.setVisibility(View.VISIBLE);
        Todayexpense.setText("Saving: " + databaseHelper.getRemainWallet());
    }


}
