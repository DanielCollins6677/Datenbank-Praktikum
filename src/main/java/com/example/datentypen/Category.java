package com.example.datentypen;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class Category {
    private String name;
    private List<Category> unterkategorien;

    public Category(String name) {
        this.name = name;
    }
}
