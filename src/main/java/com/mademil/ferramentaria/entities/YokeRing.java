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
@Table(name = "yoke_rings")
public class YokeRing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer yokeRingId;

    @NotBlank(message = "Nome não pode estar vazio")
    @Size(max = 50, message = "Nome não pode ultrapassar 50 caracteres")
    private String yokeRingName;

    @Lob
    private byte[] yokeRingImage;

    @NotBlank(message = "Localização não pode estar vazia")
    @Size(max = 100, message = "Localização não pode ultrapassar 100 caracteres")
    private String yokeRingLocation;

    private Boolean isDeleted;

    public YokeRing() {}

    public Integer getYokeRingId() {
        return yokeRingId;
    }

    public void setYokeRingId(Integer yokeRingId) {
        this.yokeRingId = yokeRingId;
    }

    public String getYokeRingName() {
        return yokeRingName;
    }

    public void setYokeRingName(String yokeRingName) {
        this.yokeRingName = yokeRingName;
    }

    public byte[] getYokeRingImage() {
        return yokeRingImage;
    }

    public void setYokeRingImage(byte[] yokeRingImage) {
        this.yokeRingImage = yokeRingImage;
    }

    public String getYokeRingLocation() {
        return yokeRingLocation;
    }

    public void setYokeRingLocation(String yokeRingLocation) {
        this.yokeRingLocation = yokeRingLocation;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}