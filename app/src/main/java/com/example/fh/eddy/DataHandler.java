package com.example.fh.eddy;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by Tim on 17.11.2014.
 * This class handles database creation as well as providing a helper
 * for database manipulation. Since the helper is only needed when a
 * database is needed it is included in this class. Only a
 * Datahandler object needs to be instantiated.
 * A separate helper object in external class is not needed.
 */
public class DataHandler {

    // Table fields
    public static final String BLOODSUGAR = "blood_sugar_value";
    public static final String BOLUSINSULIN = "bolus";
    public static final String BASEINSULIN = "base";
    public static final String CARBAMOUNT = "carb_amount";
    public static final String ACTIVITY = "activity";
    public static final String EVENT = "event";
    public static final String THE_DATE = "curr_Date";
    public static final String THE_TIME = "curr_Time";
    public static final String CREATED = "created";



    public static final String ROW_ID = "_id";

    // Database creation fields
    public static final String DATABASE_NAME = "eddydb";
    public static final String DATABASE_TABLE_NAME = "eddy_table";
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_CREATE_TABLE = "create table eddy_table (_id integer primary key autoincrement, blood_sugar_value numeric not null," +
            "bolus text, base text, carb_amount numeric, curr_Date numeric, curr_Time numeric, activity text, event text, created long);";

    // String-array holds columns of table
    private String[] allColumns = {ROW_ID, BLOODSUGAR, BOLUSINSULIN, BASEINSULIN, CARBAMOUNT,
            THE_DATE, THE_TIME,ACTIVITY, EVENT, CREATED};


    // Setup required objects
    private final Context ctx;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase eddy_db;

    // Constructor of outer class
    public DataHandler(Context ctx)
    {
        this.ctx = ctx;
        dbHelper = new DatabaseHelper(ctx);
    }

    // Method to open writable database
    public DataHandler open() throws SQLiteException
    {
        dbHelper = new DatabaseHelper(ctx);
        eddy_db = dbHelper.getWritableDatabase();
        return this;
    }

    // Method for closing the database
    public void closeDatabase()
    {
        dbHelper.close();
    }

    // Method to insert new data
    // Content = KeyValue Pairs Key = Column Value = Contents in Column
    public EntryData insertNewData(int new_blood_sugar_value, String new_bolus, String new_base, String new_carb_amount,
                                      String curr_time, String curr_date , String new_activity, String curr_event, long created)
    {
        ContentValues content = new ContentValues();

        content.put(BLOODSUGAR, new_blood_sugar_value);
        content.put(BOLUSINSULIN, new_bolus);
        content.put(BASEINSULIN, new_base);
        content.put(CARBAMOUNT, new_carb_amount);
        content.put(THE_DATE, curr_date);
        content.put(THE_TIME, curr_time);
        content.put(ACTIVITY, new_activity);
        content.put(EVENT, curr_event);
        content.put(CREATED,created);


        // Create entry row id, insert content into database
        long newEntryID = eddy_db.insert(DATABASE_TABLE_NAME, null, content);

        Cursor cursor = eddy_db.query(DATABASE_TABLE_NAME,
                allColumns, ROW_ID + " = " + newEntryID, null,
                null, null, null);
        cursor.moveToFirst();
        // Daten in das EintragDatenobjekt geschrieben ueber cursorToValues und ein neuer Eintrag returned
        EntryData newEntry = cursorToValues(cursor);
        cursor.close();
        return newEntry;

    }
    // Delete a single entry from the database
    public void deleteSingleEntry(EntryData entryData)
    {
        long id = entryData.getId();
        System.out.println("Eintrag mit ID " + id + "wurde geloescht.");
        eddy_db.delete(DATABASE_TABLE_NAME, ROW_ID + " = " + id, null);
    }

    public void deleteAllEntries()
    {
        eddy_db.delete(DATABASE_TABLE_NAME,null,null);
    }


 public EntryData getSingleEntry (long id)
 {
     EntryData singleEntry = new EntryData();

     Cursor cursor = eddy_db.query(DATABASE_TABLE_NAME, allColumns, ROW_ID + " = " + id, null, null, null, null);

     if (cursor != null && cursor.moveToFirst())
     {
             singleEntry.setId(cursor.getLong(0));
             singleEntry.setBloodSugarValue(cursor.getInt(1));
             singleEntry.setBolus(cursor.getString(2));
             singleEntry.setBaseInsulin(cursor.getString(3));
             singleEntry.setCarbAmount(cursor.getString(4));
             singleEntry.setTheDate(cursor.getString(5));
             singleEntry.setDaytime(cursor.getString(6));
             singleEntry.setActivity(cursor.getString(7));
             singleEntry.setEvent(cursor.getString(8));
             singleEntry.setUnix_time(cursor.getLong(9));
             //cursor.moveToNext();
     }

     cursor.close();

     return singleEntry;
 }

    public void updateSingleEntry (long id, int new_blood_sugar_value, String new_bolus, String new_base, String new_carb_amount,
                                           String curr_time, String curr_date , String new_activity, String curr_event)
    {
        ContentValues updatedContent = new ContentValues();

        updatedContent.put(BLOODSUGAR, new_blood_sugar_value);
        updatedContent.put(BOLUSINSULIN, new_bolus);
        updatedContent.put(BASEINSULIN, new_base);
        updatedContent.put(CARBAMOUNT, new_carb_amount);
        updatedContent.put(THE_DATE, curr_date);
        updatedContent.put(THE_TIME, curr_time);
        updatedContent.put(ACTIVITY, new_activity);
        updatedContent.put(EVENT, curr_event);

        eddy_db.update(DATABASE_TABLE_NAME, updatedContent, ROW_ID + " = " + id, null);

    }
    // Get all entries , put in List, used by Adapter in ListView
    public List<EntryData> getEveryEntry() {
        List<EntryData> everyEntry = new ArrayList<EntryData>();

        Cursor cursor = eddy_db.query(DATABASE_TABLE_NAME,
                allColumns, null, null, null, null, CREATED + " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            EntryData eintrag = cursorToValues(cursor);
            everyEntry.add(eintrag);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return everyEntry;
    }

    public List<EntryData> getEveryEntryUnsorted() {
        List<EntryData> everyEntry = new ArrayList<EntryData>();

        Cursor cursor = eddy_db.query(DATABASE_TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            EntryData eintrag = cursorToValues(cursor);
            everyEntry.add(eintrag);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return everyEntry;
    }

    public List<EntryData> getEntryUntil(long until) {
        List<EntryData> everyEntry = new ArrayList<EntryData>();

        String whereClause = "created >= ?";
        String[] whereArgs = new String[] {String.valueOf(until)};

        Cursor cursor = eddy_db.query(DATABASE_TABLE_NAME,
                allColumns, whereClause, whereArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            EntryData eintrag = cursorToValues(cursor);
            everyEntry.add(eintrag);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return everyEntry;
    }

    // Setting entry data based on cursor position
    private EntryData cursorToValues(Cursor cursor)
    {
        EntryData entry = new EntryData();

        entry.setId(cursor.getLong(0));
        entry.setBloodSugarValue(cursor.getInt(1));
        entry.setBolus(cursor.getString(2));
        entry.setBaseInsulin(cursor.getString(3));
        entry.setCarbAmount(cursor.getString(4));
        entry.setTheDate(cursor.getString(5));
        entry.setDaytime(cursor.getString(6));
        entry.setActivity(cursor.getString(7));
        entry.setEvent(cursor.getString(8));
        entry.setUnix_time(cursor.getLong(9));

        return entry;
    }

    //  Begin inner class
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        //Constructor inner class
        //Init Database
        public DatabaseHelper(Context ctx )
        {
            super(ctx, DATABASE_NAME, null,DATABASE_VERSION);
        }

        @Override
        // Once database is created the create table is executed
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try
            {
                sqLiteDatabase.execSQL(DATABASE_CREATE_TABLE);
            }
            catch(SQLiteException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
        {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS eddy_table");
            onCreate(sqLiteDatabase);
        }

    } // Ende inner class DataBaseHelper


} // End class DataHandler