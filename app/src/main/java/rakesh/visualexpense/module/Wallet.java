package rakesh.visualexpense.module;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rakesh on 22/03/16.
 */
public class Wallet {

    public int Id;
    public String Amount;
    public String Date;
    public String LatestExpense;
public String Note;

    public Wallet(){


    }

        public Wallet(String amount, String note){
            this.Amount=amount;
            this.Note=note;
            }






    //set values in wallet table
    public void setid(int id){
    this.Id=id;
    }

    public void setamount(String amount){
        this.Amount=amount;
    }

    public void setNote(String note){
        this.Amount=note;
    }
    public void setdatedb(String date ){
        this.Date=date;
    }

    public void setLatestExpense(String latestExpense ){
        this.Date=latestExpense;
    }

    public String setDate(){

        return Date;
    }
//get values from wallet table

    public int getId(){

        return Id;

    }

    public String getAmount()
    {
        return Amount;

    }

    public String getDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "d-MMM-yyyy h:mm a", Locale.getDefault());
        java.util.Date date = new Date();
        return dateFormat.format(date);

    }
    public String getNote()
    {
        return Note;

    }

    public String getLatestExpense()
    {
        return LatestExpense;


    }




}

