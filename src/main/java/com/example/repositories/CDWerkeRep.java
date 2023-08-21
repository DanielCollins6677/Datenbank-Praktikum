package com.example.repositories;

import com.example.entities.CDWerke;
import com.example.entities.CDWerkeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CDWerkeRep extends JpaRepository<CDWerke, CDWerkeId> {
    List<CDWerke> findAllByProdnr(String prodnr);
}
