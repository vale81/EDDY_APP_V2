package com.example.fh.eddy;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Tim on 17.11.2014.
 * This class handles database creation as well as providing a helper
 * for database manipulation. Since the helper is only needed when a
 * database is needed it is included in this class. Only a
 * Datahandler object needs to be instantiated.
 * A separate helper object is not needed.
 */
public class DataHandler {

    // Table fields
    public static final String BLOODSUGAR = "blood_sugar_value";
    public static final String BOLUSINSULIN = "bolus";
    public static final String BASEINSULIN = "base";
    public static final String CARBAMOUNT = "carb_amount";
    public static final String ACTIVITY = "activity";
    public static final String EVENT = "event";
    public static final String THE_DATE = "current_Date";
    public static final String THE_TIME = "current_Time";
    public static final String CREATED_TIME = "created_time";


    public static final String ROW_ID = "_id";

    // Database creation fields
    public static final String DATABASE_NAME = "eddydb";
    public static final String DATABASE_TABLE_NAME = "eddy_table";
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_CREATE_TABLE = "create table eddy_table (_id integer primary key autoincrement, blood_sugar_value numeric not null," +
            "bolus text, base text, carb_amount numeric, event text, current_Time numeric, current_Date numeric, activity text, created_time DEFAULT CURRENT_TIMESTAMP);";

    // String-array holds columns of table
    private String[] allColumns = {ROW_ID, BLOODSUGAR, BOLUSINSULIN, BASEINSULIN, CARBAMOUNT,
            ACTIVITY, EVENT, THE_DATE, THE_TIME, CREATED_TIME};


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

    // Method to open database
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

    // Methode to insert new data
    // Content = KeyValue Pairs Key = Column Value = Contents in Column
    public EintragDaten insertNewData(int new_blood_sugar_value, String new_bolus, String new_base, String new_carb_amount,
                                      String new_activity, String curr_event, String curr_date, String curr_time)
    {
        ContentValues content = new ContentValues();
        content.put(BLOODSUGAR, new_blood_sugar_value);
        content.put(BOLUSINSULIN, new_bolus);
        content.put(BASEINSULIN, new_base);
        content.put(CARBAMOUNT, new_carb_amount);
        content.put(EVENT, curr_event);
        content.put(THE_TIME, curr_time);
        content.put(THE_DATE, curr_date);
        content.put(ACTIVITY, new_activity);


        // Einfuege ID erstellen und Content-insert
        long newEntryID = eddy_db.insert(DATABASE_TABLE_NAME, null, content);
        // Alle Spalten in Cursorobjekt geladen, Cursor dann uebergeben
        // Jeder neue Eintrag bekommt eigene ID
        // EintragID an Cursorstelle 0
        Cursor cursor = eddy_db.query(DATABASE_TABLE_NAME,
                allColumns, ROW_ID + " = " + newEntryID, null,
                null, null, null);
        cursor.moveToFirst();
        // Daten in das EintragDatenobjekt geschrieben ueber cursorToValues und ein neuer Eintrag returned
        EintragDaten newEntry = cursorToValues(cursor);
        cursor.close();
        return newEntry;

    }
    // Loescht einzelne Eintraege
    public void deleteSingleEntry(EintragDaten entry)
    {
        long id = entry.getId();
        System.out.println("Eintrag mit ID " + id + "wurde geloescht.");
        eddy_db.delete(DATABASE_TABLE_NAME, ROW_ID + " = " + id, null);
    }

    // Alle Eintraege holen und in Liste packen von Adapter fuer ListView genutzt
    public List<EintragDaten> getEveryEntry() {
        List<EintragDaten> everyEntry = new ArrayList<EintragDaten>();

        Cursor cursor = eddy_db.query(DATABASE_TABLE_NAME,
                allColumns, null, null, null, null, CREATED_TIME + " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            EintragDaten eintrag = cursorToValues(cursor);
            everyEntry.add(eintrag);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return everyEntry;
    }
    // Methode um Werte an der Stelle des Cursors zu holen und damit Eintragsdaten setzen
    private EintragDaten cursorToValues(Cursor cursor)
    {
        EintragDaten entry = new EintragDaten();

        entry.setId(cursor.getLong(0));
        entry.setBloodSugarValue(cursor.getInt(1));
        entry.setBolus(cursor.getString(2));
        entry.setBaseInsulin(cursor.getString(3));
        entry.setCarbAmount(cursor.getString(4));
        entry.setActivity(cursor.getString(5));
        entry.setEvent(cursor.getString(6));
        entry.setTheDate(cursor.getString(7));
        entry.setDaytime(cursor.getString(8));

        return entry;
    }

    //  Begin inner class
    private static class DatabaseHelper extends SQLiteOpenHelper {


        //Constructor inner Class
        //Init Database
        public DatabaseHelper(Context ctx )
        {
            super(ctx, DATABASE_NAME, null,DATABASE_VERSION);
        }


        @Override
        // Once databse is created the create table is executed
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