package com.example.repositories;

import com.example.entities.DVDFormat;
import com.example.entities.DVDFormatId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DVDFormatRep extends JpaRepository<DVDFormat, DVDFormatId> {
    List<DVDFormat> findAllByProdnr(String prodnr);
}
