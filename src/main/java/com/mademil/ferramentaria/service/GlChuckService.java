package com.mademil.ferramentaria.service;

import com.mademil.ferramentaria.repositories.GlChuckRepository;
import com.mademil.ferramentaria.entities.GlChuck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class GlChuckService {
    @Autowired
    private GlChuckRepository GlChuckRepository;

    public List<GlChuck> getAllGlChucks(){
        return GlChuckRepository.findAll();
    }

    public List<GlChuck> getAllActiveGlChucks(){
        return GlChuckRepository.findAllByIsDeletedFalse();
    }

    public Optional<GlChuck> getGlChuckById(Integer id){
        return GlChuckRepository.findById(id);
    }

    public void saveGlChuck(GlChuck GlChuck){
        GlChuckRepository.save(GlChuck);
    }

    public void deleteGlChuck(GlChuck GlChuck){
        GlChuckRepository.delete(GlChuck);
    }
}