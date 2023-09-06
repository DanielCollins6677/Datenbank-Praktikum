package com.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Buch {
    @Id
    @Column
    private String prodnr;

    @Column
    private int seitenzahl;

    @Column
    private Date erscheinungsjahr;

    @Column
    private String isbn;

}
