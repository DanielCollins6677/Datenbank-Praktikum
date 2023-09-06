package com.example.repositories;

import com.example.entities.DVDBeteiligt;
import com.example.entities.DVDBeteiligtId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DVDBeteiligtRep extends JpaRepository<DVDBeteiligt, DVDBeteiligtId> {
    List<DVDBeteiligt> findAllByProdnr(String prodnr);
}
