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
 * Diese Klasse implementiert die Datenbank für die Speicherung der Daten
 * aus dem Eintragsformular.
 */
public class DataHandler {

    // Variablen fuer den Table
    public static final String BLUTZUCKERWERT = "blutzuckerwert";
    public static final String BOLUSINSULIN = "bolus";
    public static final String BASISINSULIN = "basis";
    public static final String KOHLENHYDRATMENGE = "kohlenhydratmenge";
    public static final String AKTIVITAET = "aktivitaet";
    public static final String NOTIZ = "notiz";
    public static final String DAS_DATUM = "datum";
    public static final String DIE_UHRZEIT = "uhrzeit";
    public static final String DATE_TIME = "datetime";


    public static final String ROW_ID = "_id";

    // Variablen fuer die Datenbank
    public static final String DATABASE_NAME = "eddydb";
    public static final String DATABASE_TABLE_NAME = "eddy_table";
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_CREATE_TABLE = "create table eddy_table (_id integer primary key autoincrement, blutzuckerwert numeric not null," +
            "bolus text, basis text, kohlenhydratmenge numeric, notiz text, uhrzeit numeric, datum numeric, aktivitaet text, datetime DEFAULT CURRENT_TIMESTAMP);";

    // String Array zum halten aller Spalten der DB
    private String[] allColumns = {ROW_ID, BLUTZUCKERWERT, BOLUSINSULIN, BASISINSULIN, KOHLENHYDRATMENGE,
            AKTIVITAET, NOTIZ, DAS_DATUM, DIE_UHRZEIT, DATE_TIME};


    // Noetigen Objekte anlegen
    private final Context ctx;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase eddy_db;

    // Konstruktor fuer auessere Klasse
    public DataHandler(Context ctx)
    {
        this.ctx = ctx;
        dbHelper = new DatabaseHelper(ctx);
    }

    // Methode zum oeffnen der Datenbank damit Eintraege gespeichert werden koennen
    public DataHandler open() throws SQLiteException
    {
        dbHelper = new DatabaseHelper(ctx);
        eddy_db = dbHelper.getWritableDatabase();
        return this;
    }

    // Methode zum Schliessen der Datenbank
    public void closeDatabase()
    {
        dbHelper.close();
    }

    // Methode zum Einfuegen von Daten
    // Content = KeyValue Pairs Key = Spalte Value = Inhalt in Spalte
    public EintragDaten insertNewData(int blutzuckerwert, String bolus, String basis, String kohlenhydratmenge,
                                      String aktvitaet, String notiz, String datum, String uhrzeit)
    {
        ContentValues content = new ContentValues();
        content.put(BLUTZUCKERWERT, blutzuckerwert);
        content.put(BOLUSINSULIN, bolus);
        content.put(BASISINSULIN, basis);
        content.put(KOHLENHYDRATMENGE, kohlenhydratmenge);
        content.put(NOTIZ, notiz);
        content.put(DIE_UHRZEIT, uhrzeit);
        content.put(DAS_DATUM, datum);
        content.put(AKTIVITAET, aktvitaet);


        // Einfuege ID erstellen und Content-insert
        long eintragID = eddy_db.insert(DATABASE_TABLE_NAME, null, content);
        // Alle Spalten in Cursorobjekt geladen, Cursor dann uebergeben
        // Jeder neue Eintrag bekommt eigene ID
        // EintragID an Cursorstelle 0
        Cursor cursor = eddy_db.query(DATABASE_TABLE_NAME,
                allColumns, ROW_ID + " = " + eintragID, null,
                null, null, null);
        cursor.moveToFirst();
        // Daten in das EintragDatenobjekt geschrieben ueber cursorToValues und ein neuer Eintrag returned
        EintragDaten neuerEintrag = cursorToValues(cursor);
        cursor.close();
        return neuerEintrag;

    }
    // Loescht einzelne Eintraege
    public void deleteEintrag(EintragDaten eintrag)
    {
        long id = eintrag.getId();
        System.out.println("Eintrag mit ID " + id + "wurde geloescht.");
        eddy_db.delete(DATABASE_TABLE_NAME, ROW_ID + " = " + id, null);
    }

    // Alle Eintraege holen und in Liste packen von Adapter fuer ListView genutzt
    public List<EintragDaten> getJedenEintrag() {
        List<EintragDaten> alleEintraege = new ArrayList<EintragDaten>();

        Cursor cursor = eddy_db.query(DATABASE_TABLE_NAME,
                allColumns, null, null, null, null, DATE_TIME + " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            EintragDaten eintrag = cursorToValues(cursor);
            alleEintraege.add(eintrag);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return alleEintraege;
    }
    // Methode um Werte an der Stelle des Cursors zu holen und damit Eintragsdaten setzen
    private EintragDaten cursorToValues(Cursor cursor)
    {
        EintragDaten eintrag = new EintragDaten();

        eintrag.setId(cursor.getLong(0));
        eintrag.setBloodSugarValue(cursor.getInt(1));
        eintrag.setBolus(cursor.getString(2));
        eintrag.setBaseInsulin(cursor.getString(3));
        eintrag.setCarbAmount(cursor.getString(4));
        eintrag.setActivity(cursor.getString(5));
        eintrag.setNote(cursor.getString(6));
        eintrag.setTheDate(cursor.getString(7));
        eintrag.setDaytime(cursor.getString(8));

        return eintrag;
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

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
        {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS eddy_table");
            onCreate(sqLiteDatabase);
        }

    } // Ende innere Klasse DataBaseHelper


} // Ende Klasse DataHandler