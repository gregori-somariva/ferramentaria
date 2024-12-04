package com.mademil.ferramentaria.controllers;

import com.mademil.ferramentaria.entities.Chuck;
import com.mademil.ferramentaria.entities.Tool;
import com.mademil.ferramentaria.entities.Template;
import com.mademil.ferramentaria.entities.Vise;
import com.mademil.ferramentaria.service.ChuckService;
import com.mademil.ferramentaria.service.TemplateService;
import com.mademil.ferramentaria.service.ViseService;
import com.mademil.ferramentaria.service.ToolService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@RestController
@RequestMapping("/imagem")
public class ImageController {

    @Autowired
    private ChuckService chuckService;
    
    @Autowired
    private ToolService toolService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private ViseService viseService;

    @GetMapping("/castanha/{id}")
    public ResponseEntity<byte[]> getChuckImage(@PathVariable("id") Integer id) {

        Optional<Chuck> chuckOptional = chuckService.getChuckById(id);

        if (chuckOptional.isPresent()) {
            Chuck chuck = chuckOptional.get();
            byte[] imageBytes = chuck.getChuckImage();
            
            if (imageBytes != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG) 
                        .body(imageBytes);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/ferramenta/{id}")
    public ResponseEntity<byte[]> getToolImage(@PathVariable("id") Integer id) {
        
        Optional<Tool> toolOptional = toolService.getToolById(id);

        if (toolOptional.isPresent()) {
            Tool tool = toolOptional.get();
            byte[] imageBytes = tool.getToolImage();
            
            if (imageBytes != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG) 
                        .body(imageBytes);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/gabarito/{id}")
    public ResponseEntity<byte[]> getTemplateImage(@PathVariable("id") Integer id) {

        Optional<Template> templateOptional = templateService.getTemplateById(id);

        if (templateOptional.isPresent()) {
            Template template = templateOptional.get();
            byte[] imageBytes = template.getTemplateImage();
            
            if (imageBytes != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG) 
                        .body(imageBytes);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/morsa/{id}")
    public ResponseEntity<byte[]> getViseImage(@PathVariable("id") Integer id) {

        Optional<Vise> viseOptional = viseService.getViseById(id);

        if (viseOptional.isPresent()) {
            Vise vise = viseOptional.get();
            byte[] imageBytes = vise.getViseImage();
            
            if (imageBytes != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG) 
                        .body(imageBytes);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
