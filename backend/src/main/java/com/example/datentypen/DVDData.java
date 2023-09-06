package com.example.datentypen;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class DVDData extends ProduktData{
    private int laufzeit;
    private int regioncode;
    List<Beteiligt> beteiligte;
    List<String> formate;

    public DVDData(String prodnr, String titel, double rating, int rang, String bild,
            int laufzeit, int regioncode, List<Beteiligt> beteiligte, List<String> formate){
        super(prodnr, titel, rating, rang, bild);
        this.laufzeit = laufzeit;
        this.regioncode = regioncode;
        this.beteiligte = beteiligte;
        this.formate = formate;
    }
}
