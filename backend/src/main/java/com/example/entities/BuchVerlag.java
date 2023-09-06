package com.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "buch_verlag")
@IdClass(BuchVerlagId.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BuchVerlag {
    @Id
    @Column
    private String prodnr;

    @Id
    @Column
    private String verlag;

}
