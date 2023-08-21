package com.example.repositories;

import com.example.entities.FilialeAngebot;
import com.example.entities.FilialeAngebotId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilialeAngebotRep extends JpaRepository<FilialeAngebot, FilialeAngebotId> {
    List<FilialeAngebot> findAllByProdnr(String prodnr);
}
