package com.example.repositories;

import com.example.entities.BuchVerlag;
import com.example.entities.BuchVerlagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuchVerlagRep extends JpaRepository<BuchVerlag, BuchVerlagId> {
    List<BuchVerlag> findAllByProdnr(String prodnr);
}
