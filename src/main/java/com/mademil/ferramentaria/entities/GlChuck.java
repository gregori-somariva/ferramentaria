package com.mademil.ferramentaria.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "gl_chucks")
public class GlChuck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer glChuckId;

    @NotBlank(message = "Nome não pode estar vazio")
    @Size(max = 50, message = "Nome não pode ultrapassar 50 caracteres")
    private String glChuckName;

    @Lob
    private byte[] glChuckImage;

    @NotBlank(message = "Localização não pode estar vazia")
    @Size(max = 100, message = "Localização não pode ultrapassar 100 caracteres")
    private String glChuckLocation;

    private Boolean isDeleted;

    public GlChuck() {}

    public Integer getGlChuckId() {
        return glChuckId;
    }

    public void setGlChuckId(Integer glChuckId) {
        this.glChuckId = glChuckId;
    }

    public String getGlChuckName() {
        return glChuckName;
    }

    public void setGlChuckName(String glChuckName) {
        this.glChuckName = glChuckName;
    }

    public byte[] getGlChuckImage() {
        return glChuckImage;
    }

    public void setGlChuckImage(byte[] glChuckImage) {
        this.glChuckImage = glChuckImage;
    }

    public String getGlChuckLocation() {
        return glChuckLocation;
    }

    public void setGlChuckLocation(String glChuckLocation) {
        this.glChuckLocation = glChuckLocation;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}