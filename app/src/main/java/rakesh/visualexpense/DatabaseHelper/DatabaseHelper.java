package rakesh.visualexpense.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import rakesh.visualexpense.module.Item;
import rakesh.visualexpense.module.Wallet;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ExpenseDB";
    public static final String TABLE_ITEM = "user_expense";
    public static final String TABLE_WALLET = "user_wallet";
    //user_expense table colms
    public static final String KEY_ITEM_ID = "id";
    public static final String KEY_ITEMNAME = "name";
    public static final String KEY_ITEMPRICE = "price";
    public static final String KEY_ITEMDATE = "date";
    public static final String KEY_ITEM_CATG = "category";
    public static final String KEY_ITEM_NOTE="item_note";

    //user_wallet table coloms
    public static final String KEY_WATTET_ID = "id";
    public static final String KEY_WALLET_AMOUNT = "amount";
    public static final String KEY_WATTET_DATE = "date";
    public static final String KEY_INCOME_NOTE="wallet_note";
    private static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create table " + TABLE_ITEM + "( ITEM_NO INTEGER PRIMARY KEY AUTOINCREMENT, ITEM_NAME TEXT, ITEM_PRICE INTEGER, ITEM_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        //TABLE user-expense
        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + "(" + KEY_ITEM_ID + " INTEGER PRIMARY KEY," + KEY_ITEMNAME + " TEXT," + KEY_ITEMPRICE + " TEXT," + KEY_ITEM_CATG + " TEXT," + KEY_ITEMDATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"  + KEY_ITEM_NOTE + " TEXT)";

        //TABLE user_wallet
        String CREATE_WALLET_TABLE = " CREATE TABLE " + TABLE_WALLET + "(" + KEY_WATTET_ID + " INTEGER PRIMARY KEY, " + KEY_WALLET_AMOUNT + " INTEGER, " + KEY_WATTET_DATE + " TIMESTAMAP DEFAULT CURRENT_TIMESTAMP," + KEY_INCOME_NOTE + " TEXT)";

        db.execSQL(CREATE_WALLET_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_ITEM);
        // Create tables again
        onCreate(db);

    }


    //--------------------------------------------------------user_expense----------------------------------------------------

    // Adding a new record (ITEM) to table
    public void addNewItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEMNAME, item.getItemName());
        values.put(KEY_ITEMPRICE, item.getItemprice());
        values.put(KEY_ITEMDATE, item.getDateTime());     //date method
        values.put(KEY_ITEM_CATG, item.getItemCat());//item category method
        values.put(KEY_ITEM_NOTE, item.getItemnote());//item note method

        // inserting this record
        db.insert(TABLE_ITEM, null, values);
        db.close();
    }

    // Updating a record in database table
    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEMNAME, item.getItemName());
        values.put(KEY_ITEMPRICE, item.getItemprice());
        values.put(KEY_ITEM_CATG, item.getItemCat());
        values.put(KEY_ITEM_NOTE, item.getItemnote());
        // updating row
        return db.update(TABLE_ITEM, values, KEY_ITEM_ID + " = ?", new String[]{String.valueOf(item.getId())});
    }

    // Deleting a record in database table
    public void deleteItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEM, KEY_ITEM_ID + " = ?", new String[]{String.valueOf(item.getId())});
        db.close();
    }

    // getting TOTAL EXPENSE
    public int getSumExpense() {

        SQLiteDatabase db = this.getReadableDatabase();
        int sum = 1;
        Cursor sumcursor = db.rawQuery("select sum(" + KEY_ITEMPRICE + ") from " + TABLE_ITEM, null);

        sumcursor.moveToFirst();
        if (sumcursor.getCount() > 0) {
            sum = sumcursor.getInt(0);
        }
        return sum;
    }

    //GETTING LATEST EXPENSE FORM user_expense
    public int GetLatestExpense() {

        SQLiteDatabase db = this.getReadableDatabase();

        int amount = 0;
        //SELECT price FROM user_expense ORDER BY id DESC LIMIT 1
        Cursor latestcur = db.rawQuery("select " + KEY_ITEMPRICE + " from " + TABLE_ITEM + " order by " + KEY_ITEM_ID + " desc limit 5 ", null);
        latestcur.moveToFirst();
        if (latestcur.getCount() > 0) {
            amount = latestcur.getInt(0);
        }

        return amount;


    }

    public List<Item> Getlast4() {
        List<Item> itemList = new ArrayList<>();

        String selectQuery = "select  * from " + TABLE_ITEM + " order by " + KEY_ITEM_ID + " desc limit 7 ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all table records and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setItemName(cursor.getString(1));
                item.setItemprice(cursor.getString(2));
                item.setItemdate_db(cursor.getString(3));
                item.setItemCat(cursor.getString(4));
                // Adding items to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        return itemList;
    }

    // Getting All Items in Table of Database
    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<>();

        // select query
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all table records and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setItemName(cursor.getString(1));
                item.setItemprice(cursor.getString(2));
                item.setItemdate_db(cursor.getString(3));
                item.setItemCat(cursor.getString(4));
                item.setItemnote(cursor.getString(5));

                // Adding items to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        return itemList;
    }

    // Getting All Items in Table of Database
    public List<Item> gettodayexpense() {
        List<Item> itemList = new ArrayList<>();

        //GregorianCalendar gc = new GregorianCalendar();
        //SimpleDateFormat dateFormat = new SimpleDateFormat(
        //      "d-MMM-yyyy h:mm a", Locale.getDefault());
        //Date date = new Date();
        //String today =dateFormat.format(date);

        // select query
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM + " WHERE " + KEY_ITEMDATE + " = date('now', 'start of day')";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all table records and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setItemName(cursor.getString(1));
                item.setItemprice(cursor.getString(2));
                item.setItemdate_db(cursor.getString(3));
                item.setItemCat(cursor.getString(4));
                item.setItemnote(cursor.getString(5));
                // Adding items to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        return itemList;
    }

    public int getTodaysCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(gc.getTime());

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ITEM + " WHERE " + KEY_ITEMDATE + " = DATETIME( ' " + today + " ' )", null);


        if (cursor.getCount() == 0 || !cursor.moveToFirst()) {

            try {
                throw new SQLException("No entries found");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return cursor.getCount();
    }





//---------------------------------------------------------user_wallet--------------------------------------------------------






    // Getting All incomes in Table of Database
    public List<Wallet> getAllincome() {

        List<Wallet> itemList = new ArrayList<>();

    // select query
    String selectQuery = "SELECT  * FROM " + TABLE_WALLET;

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    // looping through all table records and adding to list
    if (cursor.moveToFirst()) {
        do {
            Wallet income = new Wallet();
            income.setid(Integer.parseInt(cursor.getString(0)));
            income.setamount(cursor.getString(1));
            income.setdatedb(cursor.getString(2));

            // Adding items to list
            itemList.add(income);
        } while (cursor.moveToNext());
    }

    return itemList;
}


    // Adding a new wallet in table
    public boolean addWallet(Wallet wallet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_WALLET_AMOUNT, wallet.getAmount());
        values.put(KEY_WATTET_DATE, wallet.getDate());

        long result = db.insert(TABLE_WALLET, null, values);
        if (result == 1)
            return false;
        else
            return true;
    }

    // Updating a wallet in database table
    public int updateIncome(Wallet wallet) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WALLET_AMOUNT, wallet.getAmount());
        values.put(KEY_INCOME_NOTE, wallet.getNote());


        // updating row
        return db.update(TABLE_WALLET, values, KEY_WATTET_ID + " = ?", new String[]{String.valueOf(wallet.getId())});
    }
    // Deleting a record in database table
    public void deleteincome(Wallet wallet) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WALLET, KEY_ITEM_ID + " = ?", new String[]{String.valueOf(wallet.getId())});
        db.close();
    }

    // GETTING TOTAL WALLET AMOUNT
    public int getSumWallet() {

        SQLiteDatabase db = this.getReadableDatabase();

        int amount = 0;
        //Cursor sumcursor = db.rawQuery("select " + KEY_WALLET_AMOUNT + " from " + TABLE_WALLET, null);
        Cursor sumcursor = db.rawQuery(" select sum(" + KEY_WALLET_AMOUNT + ") from " + TABLE_WALLET, null);
        sumcursor.moveToFirst();
        if (sumcursor.getCount() > 0) {
            amount = sumcursor.getInt(0);
        }
        return amount;

    }


    //GETTING REMANING AMOUNT FROM user_wallet
    public float getRemainWallet(){

        float expensesum = (float)getSumExpense();
        float walletsum = (float)getSumWallet();
        float percentage =   walletsum-expensesum;


        return percentage;



    }
    public int getpercent(){

        float expensesum = (float)getSumExpense();
        float walletsum = (float)getSumWallet();
        float c=100;
        float percentage =   (expensesum / walletsum)*c ;
        //c= (a/b)*c;
        int per = (int)percentage;
        return per;



    }



}







