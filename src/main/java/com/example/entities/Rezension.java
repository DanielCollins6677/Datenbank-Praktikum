package com.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@IdClass(RezensionId.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rezension {
    @Id
    @Column(
            name = "kname"
    )
    private String kundenName;

    @Id
    @Column
    private String prodnr;

    @Column
    private int rating;

    @Column
    private int helpful;

    @Column(
            name = "zeitpunkt"
    )
    private Date date;

    @Column
    private String kommentar;
}
