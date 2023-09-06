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
@IdClass(KategorieOrdnungId.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KategorieOrdnung {
    @Id
    @Column
    private String oberkategorie;

    @Id
    @Column
    private String unterkategorie;
}
