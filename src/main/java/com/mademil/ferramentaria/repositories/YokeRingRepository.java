package com.mademil.ferramentaria.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mademil.ferramentaria.entities.YokeRing;

import java.util.List;

public interface YokeRingRepository extends JpaRepository<YokeRing, Integer> {

    List<YokeRing> findAllByIsDeletedFalse();  
}
