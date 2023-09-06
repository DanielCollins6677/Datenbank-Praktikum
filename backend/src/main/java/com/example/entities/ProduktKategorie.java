package com.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@IdClass(ProduktKategorieId.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProduktKategorie {
    @Id
    @Column
    private String prodnr;

    @Id
    @Column(
            name = "kat_name"
    )
    private String kategorieName;
}
