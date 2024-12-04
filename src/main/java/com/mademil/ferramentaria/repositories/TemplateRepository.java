package com.mademil.ferramentaria.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mademil.ferramentaria.entities.Template;

import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, Integer> {

    List<Template> findAllByIsDeletedFalse();  
}
