package com.example.datentypen;

import com.example.entities.Buch;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class BuchData extends ProduktData{
    private int seitenzahl;
    private Date erscheinungjahr;
    private String isbn;
    private List<String> autoren;
    private List<String> verlage;

    public BuchData(String prodnr, String titel, double rating, int rang, String bild,
                    int seitenzahl, Date erscheinungjahr, String isbn, List<String > autoren, List< String> verlage){
        super(prodnr, titel, rating, rang, bild);
        this.seitenzahl = seitenzahl;
        this.erscheinungjahr = erscheinungjahr;
        this.isbn = isbn;
        this.autoren = autoren;
        this.verlage = verlage;
    }
}
