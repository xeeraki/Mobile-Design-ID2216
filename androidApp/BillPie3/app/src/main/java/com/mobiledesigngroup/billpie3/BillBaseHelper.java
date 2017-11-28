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

    // TODO: harmonize and organize the tables (below is a first draft only)

    public static final String DATABASE_NAME = "Bill.db";

    public static final String TABLE_BILL = "Bill_table";
    public static final String TABLE_USER = "User_table";
    public static final String TABLE_EVENT = "Event_table";
    public static final String TABLE_FRIEND = "Friend_table";
    // USER_ID EVENT_ID DUE_TO AMOUNT
    public static final String TABLE_EVENT_USER_DUE = "Event_User_Due_table";

    public static final String COLUMN_ID = "ID";

    public static final String COLUMN_BILL_TITLE = "TITLE";
    public static final String COLUMN_BILL_AMOUNT= "AMOUNT";
    public static final String COLUMN_BILL_DATE= "DATE";

    public static final String COLUMN_USER_NAME = "USER_NAME";

    public static final String COLUMN_EVENT_EVENT_TITLE = "EVENT_TITLE";
    public static final String COLUMN_EVENT_SPENDING_TITLE = "SPENDING_TITLE";
    public static final String COLUMN_EVENT_SPENDING_DATE = "SPENDING_DATE";

    public static final String COLUMN_FRIEND_USER_ID = "USER_ID";
    public static final String COLUMN_FRIEND_FRIEND_NAME = "FRIEND_NAME";
    public static final String COLUMN_FRIEND_FRIEND_PHONE= "FRIEND_PHONE";

    public static final String COLUMN_EUD_USER_ID = "USER_ID";
    public static final String COLUMN_EUD_DUE_TO= "DUE_TO";
    public static final String COLUMN_EUD_AMOUNT = "AMOUNT";
    public static final String COLUMN_EUD_EVENT_ID= "EVENT_ID";

    public static final String CREATE_TABLE_BILL = "CREATE TABLE " + TABLE_BILL + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + COLUMN_BILL_TITLE + " TEXT NOT NULL, "
            + COLUMN_BILL_AMOUNT + " TEXT NOT NULL, "
            + COLUMN_BILL_DATE + " TEXT NOT NULL);";

    public static final String CREATE_TABLE_EVENT = "CREATE TABLE " + TABLE_EVENT + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + COLUMN_EVENT_EVENT_TITLE + " TEXT NOT NULL, "
            + COLUMN_EVENT_SPENDING_TITLE + " TEXT NOT NULL, "
            + COLUMN_EVENT_SPENDING_DATE + " TEXT NOT NULL);";

    public static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + COLUMN_USER_NAME + " TEXT NOT NULL);";

    public static final String CREATE_TABLE_FRIEND = "CREATE TABLE " + TABLE_FRIEND + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + COLUMN_FRIEND_USER_ID + " INTEGER NOT NULL, "
            + COLUMN_FRIEND_FRIEND_NAME + " TEXT NOT NULL, "
            + COLUMN_FRIEND_FRIEND_PHONE + " TEXT NOT NULL);";

    // Lists what the users has to pay back
    public static final String CREATE_TABLE_EVENT_USER_DUE = "CREATE TABLE " + TABLE_EVENT_USER_DUE + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + COLUMN_EUD_USER_ID + " INTEGER NOT NULL, "
            + COLUMN_EUD_EVENT_ID + " INTEGER NOT NULL, "
            + COLUMN_EUD_DUE_TO + " TEXT NOT NULL, "
            + COLUMN_EUD_AMOUNT + " TEXT NOT NULL);";

    public static final int VERSION =1;


    public BillBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(CREATE_TABLE_BILL);
       db.execSQL(CREATE_TABLE_FRIEND);
       db.execSQL(CREATE_TABLE_USER);
       db.execSQL(CREATE_TABLE_EVENT);
       db.execSQL(CREATE_TABLE_EVENT_USER_DUE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT_USER_DUE);
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
    public boolean insertFriend(int userId, String friendName, String phone){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FRIEND_USER_ID,userId);
        contentValues.put(COLUMN_FRIEND_FRIEND_NAME,friendName);
        contentValues.put(COLUMN_FRIEND_FRIEND_PHONE, phone);
        long result = db.insert(TABLE_FRIEND,null, contentValues);
        if(result == -1){
            return false;
        }
        return true;
    }

    // retrieve bills from the data base
    public Cursor getBills(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(" SELECT * FROM " + TABLE_BILL,null);
        return data;
    }

    public Cursor getDueByUserIdEventId(int userId, int eventId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(" SELECT * FROM " + TABLE_EVENT_USER_DUE +
                "WHERE USER_ID=" + userId + " AND EVENT_ID=" + eventId,null);
        return data;
    }
}
