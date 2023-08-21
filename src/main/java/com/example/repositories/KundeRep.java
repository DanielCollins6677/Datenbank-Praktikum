package com.example.repositories;

import com.example.entities.Kunde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KundeRep extends JpaRepository<Kunde,String> {
}
