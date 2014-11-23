package com.example.fh.eddy;

/**
 * Created by Tim on 21.11.2014.
 * Klasse zur modellierung der Eintraege.
 */
public class EintragDaten {
    //Row-ID
    private long id;
    //Blutzuckerwert
    private String bzWert;
    //Menge Bolusinsulin
    private String bolus;
    //Menge Basisinsulin
    private String basis;
    private String notiz;
    private String aktivitaet;
    private String datum;
    private String uhrzeit;
    private String kohlenhydratmenge;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getBzWert()
    {
        return bzWert;
    }

    public void setBzWert(String bzWert)
    {
        this.bzWert = bzWert;
    }

    public String getBolus()
    {
        return bolus;
    }

    public void setBolus(String bolus)
    {
        this.bolus = bolus;
    }

    public String getBasis()
    {
        return basis;
    }

    public void setBasis(String basis)
    {
        this.basis = basis;
    }

    public String getNotiz()
    {
        return notiz;
    }

    public void setNotiz(String notiz)
    {
        this.notiz = notiz;
    }

    public String getAktivitaet()
    {
        return aktivitaet;
    }

    public void setAktivitaet(String aktivitaet)
    {
        this.aktivitaet = aktivitaet;
    }

    public String getDatum()
    {
        return datum;
    }

    public void setDatum(String datum)
    {
        this.datum = datum;
    }

    public String getUhrzeit()
    {
        return uhrzeit;
    }

    public void setUhrzeit(String uhrzeit)
    {
        this.uhrzeit = uhrzeit;
    }

    public String getKohlenhydratmenge()
    {
        return kohlenhydratmenge;
    }

    public void setKohlenhydratmenge(String kohlenhydratmenge)
    {
        this.kohlenhydratmenge = kohlenhydratmenge;
    }

} // Ende Klasse EintragDaten