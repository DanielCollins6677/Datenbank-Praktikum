package com.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity(name = "cd_label")
@IdClass(CDLabelId.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CDLabel {
    @Id
    @Column
    private String prodnr;

    @Id
    @Column
    private String label;

}