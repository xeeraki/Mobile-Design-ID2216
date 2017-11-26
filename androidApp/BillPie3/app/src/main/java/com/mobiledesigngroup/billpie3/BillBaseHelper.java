package com.mobiledesigngroup.billpie3;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adam on 2017-11-18.
 */

public class BillBaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "Bill.db";

    public static final String TABLE_BILL = "Bill_table";
    public static final String TABLE_FRIEND = "Friend_table";

    public static final String COLUMN_ID = "ID";

    public static final String COLUMN_BILL_TITLE = "TITLE";
    public static final String COLUMN_BILL_AMOUNT= "AMOUNT";
    public static final String COLUMN_BILL_DATE= "DATE";

    public static final String COLUMN_FRIEND_NAME = "NAME";
    public static final String COLUMN_FRIEND_PHONE= "PHONE";

    public static final String CREATE_TABLE_BILL = "CREATE TABLE " + TABLE_BILL + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + COLUMN_BILL_TITLE + " TEXT NOT NULL, "
            + COLUMN_BILL_AMOUNT + " TEXT NOT NULL, "
            + COLUMN_BILL_DATE + " TEXT NOT NULL);";

    public static final String CREATE_TABLE_FRIEND = "CREATE TABLE " + TABLE_FRIEND + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + COLUMN_FRIEND_NAME + " TEXT NOT NULL, "
            + COLUMN_FRIEND_PHONE + " TEXT NOT NULL);";

    public static final int VERSION =1;


    public BillBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(CREATE_TABLE_BILL);
       db.execSQL(CREATE_TABLE_FRIEND);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND);
        onCreate(db);

    }

    // Insert a new spending
    public boolean insertSpending(String title, String amount, String date){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BILL_TITLE,title);
        contentValues.put(COLUMN_BILL_AMOUNT, amount);
        contentValues.put(COLUMN_BILL_DATE, date);
        long result = db.insert(TABLE_BILL,null, contentValues);
        if(result == -1){
            return false;
        }
        return true;
    }

    // Insert a new friend
    public boolean insertFriend(String name, String phone){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FRIEND_NAME,name);
        contentValues.put(COLUMN_FRIEND_PHONE, phone);
        long result = db.insert(TABLE_FRIEND,null, contentValues);
        if(result == -1){
            return false;
        }
        return true;
    }

// retrieve data from the data base
    public Cursor getBills(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(" SELECT * FROM " + TABLE_BILL,null);
        return data;
    }
}
