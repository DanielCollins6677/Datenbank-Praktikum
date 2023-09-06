package com.example.datentypen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AngebotList {
    private String prodnr;
    private List<String> fnamen;
    private List<Double> preise;
    private List<String> zustände;
}
