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
@Table(name = "tools")
public class Tool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer toolId;

    @NotBlank(message ="Nome não pode estar vazio")
    @Size(max = 50, message = "Nome não pode ultrapassar 50 caracteres")
    private String toolName;

    @Lob
    private byte[] toolImage;

    private Boolean isDeleted;

    public Tool() {}

    public Integer getToolId() {
        return toolId;
    }

    public void setToolId(Integer toolId) {
        this.toolId = toolId;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public byte[] getToolImage() {
        return toolImage;
    }

    public void setToolImage(byte[] toolImage) {
        this.toolImage = toolImage;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}