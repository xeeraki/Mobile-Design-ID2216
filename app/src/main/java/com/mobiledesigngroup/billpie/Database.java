package com.mobiledesigngroup.billpie;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adam on 2017-11-18.
 */

public class Database extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "Bill.db";
    public static final String TABLE_NAME = "Bill_table";
    public static final String col_0 = "ID";
    public static final String col_1 = "TITLE";
    public static final String col_2= "AMOUNT";



    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TEXT , DOUBLE ) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }


    public boolean insertData(String title, String amount){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1,title);
        contentValues.put(col_2, amount);
        long result = db.insert(TABLE_NAME,null, contentValues);
        if(result == -1){
            return false;
        }
        return true;
    }
}
