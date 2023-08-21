package com.example.repositories;

import com.example.entities.CD;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CDRep extends JpaRepository<CD, String> {
}
