package com.example.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "buch_autor")
@IdClass(BuchAutorId.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BuchAutor {
    @Id
    @Column
    private String prodnr;

    @Id
    @Column
    private String autor;

}
