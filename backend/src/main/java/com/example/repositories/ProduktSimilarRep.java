package com.example.repositories;

import com.example.entities.Produkt;
import com.example.entities.ProduktSimilar;
import com.example.entities.ProduktSimilarId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProduktSimilarRep extends JpaRepository<ProduktSimilar, ProduktSimilarId> {
    List<ProduktSimilar> findAllByProdnr1(String prodnr);

    List<ProduktSimilar> findAllByProdnr2(String prodnr);
}
