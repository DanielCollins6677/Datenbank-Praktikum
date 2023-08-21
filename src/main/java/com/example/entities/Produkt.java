package com.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Produkt {
    @Id
    @Column
    private String prodnr;

    @Column
    private String titel;

    @Column
    private double rating;

    @Column(
            name = "verkaufsrang"
    )
    private int rang;

    @Column
    private String bild;

    @Override
    public String toString() {
        return "Produkt{" +
                "prodnr='" + prodnr + '\'' +
                ", titel='" + titel + '\'' +
                ", rating=" + rating +
                ", rang=" + rang +
                ", bild='" + bild + '\'' +
                '}';
    }
}
