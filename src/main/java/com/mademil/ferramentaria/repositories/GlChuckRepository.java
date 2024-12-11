package com.mademil.ferramentaria.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mademil.ferramentaria.entities.GlChuck;

import java.util.List;

public interface GlChuckRepository extends JpaRepository<GlChuck, Integer> {

    List<GlChuck> findAllByIsDeletedFalse();  
}
