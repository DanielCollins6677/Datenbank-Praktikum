package com.example.repositories;

import com.example.entities.Buch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuchRep extends JpaRepository<Buch, String> {
}
