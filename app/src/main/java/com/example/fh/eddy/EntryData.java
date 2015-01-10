package com.example.fh.eddy;

import java.io.Serializable;

/**
 * Created by Tim on 21.11.2014.
 * This class models the entries made by the user.
 * Used as data access object
 * @author Tim
 */
public class EntryData implements Serializable{
    //Row-ID
    private long id;
    private int bloodSugarValue;
    // Bolus insulin amount
    private String bolus;
    // Base insulin amount
    private String baseInsulin;
    private String event;
    private String activity;
    private String theDate;
    private String daytime;
    private String carbAmount;
    private long unix_time;



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

    public String getEvent()
    {
        return event;
    }

    public void setEvent(String event)
    {
        this.event = event;
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

    public long getUnix_time() {
        return unix_time;
    }

    public void setUnix_time(long unix_time) {
        this.unix_time = unix_time;
    }

    /**
     * ToString used by adapter to display items in the ListView
     * Currently not used since a custom adapter is used instead
     * @return String
     */
    public String toString()
    {
        return Integer.toString(bloodSugarValue) + " " + theDate + " " + daytime + "\n " + "Event: " + event + " Activity: " + activity;
    }

} // End EntryData