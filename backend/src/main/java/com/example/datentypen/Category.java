package com.example.datentypen;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class Category implements Comparable<Category>{
    private String name;
    private List<Category> unterkategorien;
    private int value = 0;

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int compareTo(Category o) {
        if(this.value < o.getValue()) return -1;
        else if(this.value > o.getValue()) return 1;
        else return this.name.compareTo(o.getName());
    }
}
