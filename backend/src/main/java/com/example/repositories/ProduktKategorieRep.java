package com.example.repositories;

import com.example.entities.Produkt;
import com.example.entities.ProduktKategorie;
import com.example.entities.ProduktKategorieId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduktKategorieRep extends JpaRepository<ProduktKategorie, ProduktKategorieId> {
    List<ProduktKategorie> findAllByKategorieName(String kategorieName);
}
