package rakesh.visualexpense.module;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Item {

    public int id;
    public String itemname;
    public String itemprice;
    public String itemdate;
    public String itemcat;
    public String itemnote;

    public Item() {
    }

    public Item(String itemName, String itemprice, String itemcat ,String itemNote) {
        this.itemname = itemName;
        this.itemprice = itemprice;
        this.itemcat = itemcat;
        this.itemnote =itemNote;

    }

    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemname;
    }

    public String getItemprice() {
        return itemprice;
    }

    public String getItemCat() {
        return itemcat;
    }

    //For data_time
    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "d-MMM-yyyy h:mm a", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);


    }
    public String getItemnote() {
        return itemnote;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setItemName(String itemname) {
        this.itemname = itemname;
    }

    public void setItemprice(String itemprice) {
        this.itemprice = itemprice;
    }

    public void setItemCat(String itemcat) {
        this.itemcat = itemcat;
    }

    public void setItemdate_db(String itemdate) {

        this.itemdate = itemdate;
    }

    public String setItemdate() {

        return itemdate;
    }

    public void setItemnote(String itemnote) {
        this.itemnote = itemnote;
    }
}