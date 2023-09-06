package com.example.datentypen;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;


@Setter
@Getter
public class CDData extends ProduktData{
    private Date erscheinungjahr;
    private List<String> künstler;
    private List<String> label;
    private List<String> werke;

    public CDData(String prodnr, String titel, double rating, int rang, String bild,
                  Date erscheinungjahr, List<String> künstler, List<String> label, List<String> werke){
        super(prodnr, titel, rating, rang, bild);
        this.erscheinungjahr = erscheinungjahr;
        this.künstler = künstler;
        this.label = label;
        this.werke = werke;
    }
}
