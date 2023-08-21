package com.example.repositories;

import com.example.entities.Rezension;
import com.example.entities.RezensionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RezensionRep extends JpaRepository<Rezension, RezensionId> {
}
