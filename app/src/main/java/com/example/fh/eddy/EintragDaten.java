package com.example.fh.eddy;

/**
 * Created by Tim on 21.11.2014.
 * Klasse zur modellierung der Eintraege.
 */
public class EintragDaten {
    //Row-ID
    private long id;
    //Blutzuckerwert
    private int bloodSugarValue;
    //Menge Bolusinsulin
    private String bolus;
    //Menge Basisinsulin
    private String baseInsulin;
    private String note;
    private String activity;
    private String theDate;
    private String daytime;
    private String carbAmount;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public int getBloodSugarValue()
    {
        return bloodSugarValue;
    }

    public void setBloodSugarValue(int bloodSugarValue)
    {
        this.bloodSugarValue = bloodSugarValue;
    }

    public String getBolus()
    {
        return bolus;
    }

    public void setBolus(String bolus)
    {
        this.bolus = bolus;
    }

    public String getBaseInsulin()
    {
        return baseInsulin;
    }

    public void setBaseInsulin(String baseInsulin)
    {
        this.baseInsulin = baseInsulin;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getActivity()
    {
        return activity;
    }

    public void setActivity(String activity)
    {
        this.activity = activity;
    }

    public String getTheDate()
    {
        return theDate;
    }

    public void setTheDate(String theDate)
    {
        this.theDate = theDate;
    }

    public String getDaytime()
    {
        return daytime;
    }

    public void setDaytime(String daytime)
    {
        this.daytime = daytime;
    }

    public String getCarbAmount()
    {
        return carbAmount;
    }

    public void setCarbAmount(String carbAmount)
    {
        this.carbAmount = carbAmount;
    }

    // Von Adapter fuer View genutzt
    public String toString()
    {
        return Integer.toString(bloodSugarValue) + "   " + theDate + "     " + daytime;
    }

} // Ende Klasse EintragDaten