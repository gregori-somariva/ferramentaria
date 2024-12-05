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
@Table(name = "chucks")
public class Chuck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chuckId;

    @NotBlank(message = "Nome não pode estar vazio")
    @Size(max = 50, message = "Nome não pode ultrapassar 50 caracteres")
    private String chuckName;

    @Lob
    private byte[] chuckImage;

    @NotBlank(message = "Localização não pode estar vazia")
    @Size(max = 100, message = "Localização não pode ultrapassar 100 caracteres")
    private String chuckLocation;

    private Boolean isDeleted;

    public Chuck() {}

    public Integer getChuckId() {
        return chuckId;
    }

    public void setChuckId(Integer chuckId) {
        this.chuckId = chuckId;
    }

    public String getChuckName() {
        return chuckName;
    }

    public void setChuckName(String chuckName) {
        this.chuckName = chuckName;
    }

    public byte[] getChuckImage() {
        return chuckImage;
    }

    public void setChuckImage(byte[] chuckImage) {
        this.chuckImage = chuckImage;
    }

    public String getChuckLocation() {
        return chuckLocation;
    }

    public void setChuckLocation(String chuckLocation) {
        this.chuckLocation = chuckLocation;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}