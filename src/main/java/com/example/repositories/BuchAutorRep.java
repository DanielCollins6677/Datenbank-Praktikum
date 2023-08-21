package com.example.repositories;

import com.example.entities.BuchAutor;
import com.example.entities.BuchAutorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuchAutorRep extends JpaRepository<BuchAutor, BuchAutorId> {
    List<BuchAutor> findAllByProdnr(String prodnr);
}
