package com.example.repositories;


import com.example.entities.DVD;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DVDRep extends JpaRepository<DVD, String> {
}
