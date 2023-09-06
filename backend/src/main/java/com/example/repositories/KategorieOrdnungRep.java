package com.example.repositories;

import com.example.entities.KategorieOrdnung;
import com.example.entities.KategorieOrdnungId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KategorieOrdnungRep extends JpaRepository<KategorieOrdnung, KategorieOrdnungId> {

    List<KategorieOrdnung> findAllByOberkategorie(String name);
}
