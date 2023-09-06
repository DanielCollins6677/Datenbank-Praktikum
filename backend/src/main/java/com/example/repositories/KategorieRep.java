package com.example.repositories;

import com.example.entities.Kategorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KategorieRep extends JpaRepository<Kategorie,String> {
}
