package com.example.fh.eddy;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tim on 17.11.2014.
 * Diese Klasse implementiert die Datenbank für die Speicherung der Daten
 * aus dem Eintragsformular.
 */
public class DataHandler {

    // Variablen fuer den Table
    public static final String BLUTZUCKERWERT = "blutzuckerwert";
    public static final String BOLUSINSULIN = "bolus";
    public static final String BASISINSULIN = "basis";
    public static final String MAHLZEIT = "mahlzeit";
    //public static final String AKTIVITAET = "aktivitaet";
    public static final String NOTIZ = "notiz";
    public static final String DAS_DATUM = "datum";
    public static final String DIE_UHRZEIT = "uhrzeit";
    public static final String ROW_ID = "_id";

    // Variablen fuer die Datenbank
    public static final String DATABASE_NAME = "eddydb";
    public static final String DATABASE_TABLE_NAME = "eddy_table";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_CREATE_TABLE = "create table eddy_table (_id integer primary key autoincrement, blutzuckerwert text not null," +
            "bolus text, basis text, mahlzeit text, notiz text, uhrzeit text, datum text);";

    // Noetigen Objekte anlegen
    private final Context ctx;
    private DatabaseHelper dbhelper;
    private SQLiteDatabase eddy_db;

    // Konstruktor fuer auessere Klasse
    public DataHandler(Context ctx){
        this.ctx = ctx;
        dbhelper = new DatabaseHelper(ctx);
    }

    // Methode zum oeffnen der Datenbank damit Eintraege gespeichert werden koennen
    public DataHandler open() throws SQLiteException
    {
        dbhelper = new DatabaseHelper(ctx);
        eddy_db = dbhelper.getWritableDatabase();
        return this;
    }

    // Methode zum Schliessen der Datenbank
    public void closeDatabase()
    {
        dbhelper.close();
    }

    // Methode zum Einfuegen von Daten
    public long insertData(String blutzuckerwert, String bolus, String basis, String mahlzeit,
                           String notiz, String uhrzeit, String datum)
    {
        ContentValues content = new ContentValues();
        content.put(BLUTZUCKERWERT, blutzuckerwert);
        content.put(BOLUSINSULIN, bolus);
        content.put(BASISINSULIN, basis);
        content.put(MAHLZEIT, mahlzeit);
        content.put(NOTIZ, notiz);
        content.put(DIE_UHRZEIT, uhrzeit);
        content.put(DAS_DATUM, datum);

        return eddy_db.insert(DATABASE_TABLE_NAME, null, content);
    }

    // Methode um Daten zu holen, returns Cursor-Objekt
    public Cursor returnData()
    {
        return eddy_db.query(DATABASE_TABLE_NAME, new String[] {BLUTZUCKERWERT, BOLUSINSULIN, BASISINSULIN, MAHLZEIT,
                NOTIZ, DIE_UHRZEIT, DAS_DATUM}, null, null, null, null, ROW_ID);
    }

    //  Anfang innere Klasse
    private static class DatabaseHelper extends SQLiteOpenHelper {

        //Konstructor fuer innere Klasse
        //anlegen der Datenbank
        public DatabaseHelper(Context ctx )
        {
            super(ctx, DATABASE_NAME, null,DATABASE_VERSION);
        }


        @Override // sobald die Datenbank angelegt wurde = Table erstellen
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

        @Override //drop Database on upgrade
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
        {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS eddy_table");
            onCreate(sqLiteDatabase);
        }
    } // Ende innere Klasse DataBaseHelper

} // Ende Klasse DataHandler
