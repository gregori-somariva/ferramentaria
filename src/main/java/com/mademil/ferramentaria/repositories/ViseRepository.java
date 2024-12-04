package com.mademil.ferramentaria.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mademil.ferramentaria.entities.Vise;

import java.util.List;

public interface ViseRepository extends JpaRepository<Vise, Integer> {

    List<Vise> findAllByIsDeletedFalse();  
}
