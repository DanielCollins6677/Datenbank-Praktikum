package com.example.repositories;

import com.example.entities.CDLabel;
import com.example.entities.CDLabelId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CDLabelRep extends JpaRepository<CDLabel, CDLabelId> {
    List<CDLabel> findAllByProdnr(String prodnr);
}
