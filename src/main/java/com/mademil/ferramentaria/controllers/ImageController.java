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
import java.util.function.Function;

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

    private <T> ResponseEntity<byte[]> getImage(
        Function<Integer, Optional<T>> findEntityById,
        Function<T, byte[]> getImageFunction, Integer id) {
            
        Optional<T> entityOptional = findEntityById.apply(id);
        if (entityOptional.isPresent()) {
            T entity = entityOptional.get();
            byte[] imageBytes = getImageFunction.apply(entity);
            if (imageBytes != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(imageBytes);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/castanha/{id}")
    public ResponseEntity<byte[]> getChuckImage(@PathVariable("id") Integer id) {
        return getImage(chuckService::getChuckById, Chuck::getChuckImage, id);
    }

    @GetMapping("/ferramenta/{id}")
    public ResponseEntity<byte[]> getToolImage(@PathVariable("id") Integer id) {
        return getImage(toolService::getToolById, Tool::getToolImage, id);
    }

    @GetMapping("/gabarito/{id}")
    public ResponseEntity<byte[]> getTemplateImage(@PathVariable("id") Integer id) {
        return getImage(templateService::getTemplateById, Template::getTemplateImage, id);
    }

    @GetMapping("/morsa/{id}")
    public ResponseEntity<byte[]> getViseImage(@PathVariable("id") Integer id) {
        return getImage(viseService::getViseById, Vise::getViseImage, id);
    }
}
