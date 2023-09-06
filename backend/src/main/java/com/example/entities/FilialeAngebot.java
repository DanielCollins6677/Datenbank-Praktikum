package com.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="filiale_angebot")
@IdClass(FilialeAngebotId.class)
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilialeAngebot {
    @Id
    @Column
    private String fname;

    @Column
    private String prodnr;

    @Id
    @Column
    double preis;

    @Id
    @Column
    String zustand;
}
