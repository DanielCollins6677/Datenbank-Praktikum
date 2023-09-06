package com.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "produkt_ähnlich")
@IdClass(ProduktSimilarId.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProduktSimilar {
    @Id
    @Column
    private String prodnr1;

    @Id
    @Column
    private String prodnr2;
}
