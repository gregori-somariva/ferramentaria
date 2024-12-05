package com.mademil.ferramentaria.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "templates")
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer templateId;

    @NotBlank(message ="Nome não pode estar vazio")
    @Size(max = 50, message = "Nome não pode ultrapassar 50 caracteres")
    private String templateName;

    @Lob
    private byte[] templateImage;

    @NotBlank(message = "Localização não pode estar vazia")
    @Size(max = 100, message = "Localização não pode ultrapassar 100 caracteres")
    private String templateLocation;
    private Boolean isDeleted;

    public Template() {}

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public byte[] getTemplateImage() {
        return templateImage;
    }

    public void setTemplateImage(byte[] templateImage) {
        this.templateImage = templateImage;
    }

    public String getTemplateLocation() {
        return templateLocation;
    }

    public void setTemplateLocation(String templateLocation) {
        this.templateLocation = templateLocation;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}