package com.mademil.ferramentaria.dto;

import jakarta.validation.constraints.Size;

public class CompleteFormDataDTO {

    
    private Integer machineId;
    private Integer chuckId;
    private Integer secondChuckId;
    private Integer templateId;
    private Integer viseId;

    @Size(max = 50, message = "Máximo de caracteres atingido para ITEM (50)")
    private String item;

    @Size(max = 50, message = "Máximo de caracteres atingido para DESCRIÇÃO ITEM (65)")
    private String itemDescription;
    private Integer operationId;

    @Size(max = 50, message = "Máximo de caracteres atingido para PROGRAMA (50)")
    private String ncName;
    private String cycleTime;
    private Integer userId;
    private Integer holdPressure;
    private Integer chuckPressure;

    private Integer tool1Id;
    private Integer tool2Id;
    private Integer tool3Id;
    private Integer tool4Id;
    private Integer tool5Id;
    private Integer tool6Id;
    private Integer tool7Id;
    private Integer tool8Id;
    private Integer tool9Id;
    private Integer tool10Id;
    
    private Integer tool1Position;
    private Integer tool2Position;
    private Integer tool3Position;
    private Integer tool4Position;
    private Integer tool5Position;
    private Integer tool6Position;
    private Integer tool7Position;
    private Integer tool8Position;
    private Integer tool9Position;
    private Integer tool10Position;

    private Integer tool1Length;
    private Integer tool2Length;
    private Integer tool3Length;
    private Integer tool4Length;
    private Integer tool5Length;
    private Integer tool6Length;
    private Integer tool7Length;
    private Integer tool8Length;
    private Integer tool9Length;
    private Integer tool10Length;

    // Getters and setters for non-tool-related fields (no changes needed)
    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    public Integer getChuckId() {
        return chuckId;
    }

    public void setChuckId(Integer chuckId) {
        this.chuckId = chuckId;
    }

    public Integer getSecondChuckId() {
        return secondChuckId;
    }

    public void setSecondChuckId(Integer secondChuckId) {
        this.secondChuckId = secondChuckId;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getViseId() {
        return viseId;
    }

    public void setViseId(Integer viseId) {
        this.viseId = viseId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Integer getOperationId() {
        return operationId;
    }

    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }

    public String getNcName() {
        return ncName;
    }

    public void setNcName(String ncName) {
        this.ncName = ncName;
    }

    public String getCycleTime() {
        return cycleTime;
    }

    public void setCycleTime(String cycleTime) {
        this.cycleTime = cycleTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getHoldPressure() {
        return holdPressure;
    }

    public void setHoldPressure(Integer holdPressure) {
        this.holdPressure = holdPressure;
    }

    public Integer getChuckPressure() {
        return chuckPressure;
    }

    public void setChuckPressure(Integer chuckPressure) {
        this.chuckPressure = chuckPressure;
    }

    // Tools-related fields for tool 1 through 10
    public Integer getTool1Id() {
        return tool1Id;
    }

    public void setTool1Id(Integer tool1Id) {
        this.tool1Id = tool1Id;
    }

    public Integer getTool1Position() {
        return tool1Position;
    }

    public void setTool1Position(Integer tool1Position) {
        this.tool1Position = tool1Position;
    }

    public Integer getTool1Length() {
        return tool1Length;
    }

    public void setTool1Length(Integer tool1Length) {
        this.tool1Length = tool1Length;
    }

    public Integer getTool2Id() {
        return tool2Id;
    }

    public void setTool2Id(Integer tool2Id) {
        this.tool2Id = tool2Id;
    }

    public Integer getTool2Position() {
        return tool2Position;
    }

    public void setTool2Position(Integer tool2Position) {
        this.tool2Position = tool2Position;
    }

    public Integer getTool2Length() {
        return tool2Length;
    }

    public void setTool2Length(Integer tool2Length) {
        this.tool2Length = tool2Length;
    }

    public Integer getTool3Id() {
        return tool3Id;
    }
    
    public void setTool3Id(Integer tool3Id) {
        this.tool3Id = tool3Id;
    }
    
    public Integer getTool3Position() {
        return tool3Position;
    }
    
    public void setTool3Position(Integer tool3Position) {
        this.tool3Position = tool3Position;
    }
    
    public Integer getTool3Length() {
        return tool3Length;
    }
    
    public void setTool3Length(Integer tool3Length) {
        this.tool3Length = tool3Length;
    }
    
    // Tool 4 Getters and Setters
    public Integer getTool4Id() {
        return tool4Id;
    }
    
    public void setTool4Id(Integer tool4Id) {
        this.tool4Id = tool4Id;
    }
    
    public Integer getTool4Position() {
        return tool4Position;
    }
    
    public void setTool4Position(Integer tool4Position) {
        this.tool4Position = tool4Position;
    }
    
    public Integer getTool4Length() {
        return tool4Length;
    }
    
    public void setTool4Length(Integer tool4Length) {
        this.tool4Length = tool4Length;
    }
    
    // Tool 5 Getters and Setters
    public Integer getTool5Id() {
        return tool5Id;
    }
    
    public void setTool5Id(Integer tool5Id) {
        this.tool5Id = tool5Id;
    }
    
    public Integer getTool5Position() {
        return tool5Position;
    }
    
    public void setTool5Position(Integer tool5Position) {
        this.tool5Position = tool5Position;
    }
    
    public Integer getTool5Length() {
        return tool5Length;
    }
    
    public void setTool5Length(Integer tool5Length) {
        this.tool5Length = tool5Length;
    }
    
    // Tool 6 Getters and Setters
    public Integer getTool6Id() {
        return tool6Id;
    }
    
    public void setTool6Id(Integer tool6Id) {
        this.tool6Id = tool6Id;
    }
    
    public Integer getTool6Position() {
        return tool6Position;
    }
    
    public void setTool6Position(Integer tool6Position) {
        this.tool6Position = tool6Position;
    }
    
    public Integer getTool6Length() {
        return tool6Length;
    }
    
    public void setTool6Length(Integer tool6Length) {
        this.tool6Length = tool6Length;
    }
    
    // Tool 7 Getters and Setters
    public Integer getTool7Id() {
        return tool7Id;
    }
    
    public void setTool7Id(Integer tool7Id) {
        this.tool7Id = tool7Id;
    }
    
    public Integer getTool7Position() {
        return tool7Position;
    }
    
    public void setTool7Position(Integer tool7Position) {
        this.tool7Position = tool7Position;
    }
    
    public Integer getTool7Length() {
        return tool7Length;
    }
    
    public void setTool7Length(Integer tool7Length) {
        this.tool7Length = tool7Length;
    }
    
    // Tool 8 Getters and Setters
    public Integer getTool8Id() {
        return tool8Id;
    }
    
    public void setTool8Id(Integer tool8Id) {
        this.tool8Id = tool8Id;
    }
    
    public Integer getTool8Position() {
        return tool8Position;
    }
    
    public void setTool8Position(Integer tool8Position) {
        this.tool8Position = tool8Position;
    }
    
    public Integer getTool8Length() {
        return tool8Length;
    }
    
    public void setTool8Length(Integer tool8Length) {
        this.tool8Length = tool8Length;
    }
    
    // Tool 9 Getters and Setters
    public Integer getTool9Id() {
        return tool9Id;
    }
    
    public void setTool9Id(Integer tool9Id) {
        this.tool9Id = tool9Id;
    }
    
    public Integer getTool9Position() {
        return tool9Position;
    }
    
    public void setTool9Position(Integer tool9Position) {
        this.tool9Position = tool9Position;
    }
    
    public Integer getTool9Length() {
        return tool9Length;
    }
    
    public void setTool9Length(Integer tool9Length) {
        this.tool9Length = tool9Length;
    }
    
    // Tool 10 Getters and Setters
    public Integer getTool10Id() {
        return tool10Id;
    }
    
    public void setTool10Id(Integer tool10Id) {
        this.tool10Id = tool10Id;
    }
    
    public Integer getTool10Position() {
        return tool10Position;
    }
    
    public void setTool10Position(Integer tool10Position) {
        this.tool10Position = tool10Position;
    }
    
    public Integer getTool10Length() {
        return tool10Length;
    }
    
    public void setTool10Length(Integer tool10Length) {
        this.tool10Length = tool10Length;
    }
}
