package com.mademil.ferramentaria.service;

import com.mademil.ferramentaria.entities.YokeRing;
import com.mademil.ferramentaria.repositories.YokeRingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class YokeRingService {
    @Autowired
    private YokeRingRepository yokeRingRepository;

    public List<YokeRing> getAllYokeRings(){
        return yokeRingRepository.findAll();
    }

    public List<YokeRing> getAllActiveYokeRings(){
        return yokeRingRepository.findAllByIsDeletedFalse();
    }

    public Optional<YokeRing> getYokeRingById(Integer id){
        return yokeRingRepository.findById(id);
    }

    public void saveYokeRing(YokeRing yokeRing){
        yokeRingRepository.save(yokeRing);
    }

    public void deleteYokeRing(YokeRing yokeRing){
        yokeRingRepository.delete(yokeRing);
    }
}