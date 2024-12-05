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
@Table(name = "vises")
public class Vise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer viseId;

    @NotBlank(message ="Nome não pode estar vazio")
    @Size(max = 50, message = "Nome não pode ultrapassar 50 caracteres")
    private String viseName;
    
    @Lob
    private byte[] viseImage;

    @NotBlank(message = "Localização não pode estar vazia")
    @Size(max = 100, message = "Localização não pode ultrapassar 100 caracteres")
    private String viseLocation;
    private Boolean isDeleted;

    public Vise() {}

    public Integer getViseId() {
        return viseId;
    }

    public void setViseId(Integer viseId) {
        this.viseId = viseId;
    }

    public String getViseName() {
        return viseName;
    }

    public void setViseName(String viseName) {
        this.viseName = viseName;
    }

    public byte[] getViseImage() {
        return viseImage;
    }

    public void setViseImage(byte[] viseImage) {
        this.viseImage = viseImage;
    }

    public String getViseLocation() {
        return viseLocation;
    }

    public void setViseLocation(String viseLocation) {
        this.viseLocation = viseLocation;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}