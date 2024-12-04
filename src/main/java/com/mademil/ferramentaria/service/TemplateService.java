package com.mademil.ferramentaria.service;

import com.mademil.ferramentaria.repositories.TemplateRepository;
import com.mademil.ferramentaria.entities.Template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class TemplateService {
    @Autowired
    private TemplateRepository templateRepository;

    public List<Template> getAllTemplates(){
        return templateRepository.findAll();
    }

    public List<Template> getAllActiveTemplates(){
        return templateRepository.findAllByIsDeletedFalse();
    }

    public Optional<Template> getTemplateById(Integer id){
        return templateRepository.findById(id);
    }

    public void saveTemplate(Template template){
        templateRepository.save(template);
    }

    public void deleteTemplate(Template template){
        templateRepository.delete(template);
    }
}