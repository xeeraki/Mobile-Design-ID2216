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
    public static final String TABLE_NAME = "Bill_table";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_AMOUNT= "AMOUNT";
    public static final String COLUMN_DATE= "DATE";
    public static final int VERSION =1;


    public BillBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
               + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
               + COLUMN_TITLE + " TEXT NOT NULL, "
               + COLUMN_AMOUNT + " TEXT NOT NULL, "
               + COLUMN_DATE + " TEXT NOT NULL);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

//insert data to the data base
    public boolean insertData(String title, String amount){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE,title);
        contentValues.put(COLUMN_AMOUNT, amount);
        long result = db.insert(TABLE_NAME,null, contentValues);
        if(result == -1){
            return false;
        }
        return true;
    }

    // Insert a new spending
    public boolean insertSpending(String title, String amount, String date){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE,title);
        contentValues.put(COLUMN_AMOUNT, amount);
        contentValues.put(COLUMN_DATE, date);
        long result = db.insert(TABLE_NAME,null, contentValues);
        if(result == -1){
            return false;
        }
        return true;
    }
// retrieve data from the data base
    public Cursor getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(" SELECT * FROM " + TABLE_NAME,null);
        return data;
    }
}
