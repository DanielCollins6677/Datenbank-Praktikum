package com.example.datentypen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ProduktData {
    private String prodnr;
    private String titel;
    private double rating;
    private int rang;
    private String bild;
}
