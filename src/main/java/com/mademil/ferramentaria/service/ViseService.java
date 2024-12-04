package com.mademil.ferramentaria.service;

import com.mademil.ferramentaria.repositories.ViseRepository;
import com.mademil.ferramentaria.entities.Vise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class ViseService {
    @Autowired
    private ViseRepository viseRepository;

    public List<Vise> getAllVises(){
        return viseRepository.findAll();
    }

    public List<Vise> getAllActiveVises(){
        return viseRepository.findAllByIsDeletedFalse();
    }

    public Optional<Vise> getViseById(Integer id){
        return viseRepository.findById(id);
    }

    public void saveVise(Vise Vise){
        viseRepository.save(Vise);
    }

    public void deleteVise(Vise Vise){
        viseRepository.delete(Vise);
    }
}