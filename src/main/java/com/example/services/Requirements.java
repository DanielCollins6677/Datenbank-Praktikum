package com.example.services;

import com.example.datentypen.Category;
import com.example.datentypen.ProduktData;
import com.example.entities.FilialeAngebot;
import com.example.entities.Kunde;
import com.example.entities.Produkt;

import java.util.List;

public interface Requirements {

    void init();

    void finish();

    ProduktData getProdukt(String prodnr);

    List<ProduktData> getProdukts(String Pattern);

    Category getCategoryTree(boolean extern);

    List<ProduktData> getProductsByCategoryPath(String path);

    List<ProduktData> getTopProducts(int limit);

    List<ProduktData> getSimilarCheaperProduct(String prodnr);

    void addNewReview(String kname, String prodnr, int rating, String kommentar);

    List<Kunde> getTrolls(double grenze);

    List<FilialeAngebot> getOffers(String prodnr);


}
