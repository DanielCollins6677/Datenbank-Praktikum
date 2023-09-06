package com.example.repositories;

import com.example.entities.CDKünstler;
import com.example.entities.CDKünstlerId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CDKünstlerRep extends JpaRepository<CDKünstler, CDKünstlerId> {
    List<CDKünstler> findAllByProdnr(String prodnr);
}
