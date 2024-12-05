package com.mademil.ferramentaria.dto;

import com.mademil.ferramentaria.entities.FormSubmission;

import java.time.LocalDateTime;

// This DTO serves to populate the table on
// active-sheets.html and legacy-sheets.html
// All the data in this DTO is necessary
// to provide the full functionality of the view
public class FormSubmissionDTO {
    
    private Integer submissionId;
    private Integer machineId; 
    private String machineName;
    private String item; 
    private String itemDescription;
    private Integer operationId; 
    private String cycleTime;
    private Integer userId;
    private String userFullName;
    private LocalDateTime createdAt;
    private String formType;

    public FormSubmissionDTO(FormSubmission submission, String machineName, String cycleTime, String userFullName) {
        this.submissionId = submission.getSubmissionId();
        this.machineId = submission.getMachineId();
        this.machineName = machineName;
        this.item = submission.getItem();
        this.itemDescription = submission.getItemDescription();
        this.operationId = submission.getOperationId();
        this.cycleTime = cycleTime;
        this.userId = submission.getUserId();
        this.userFullName = userFullName;
        this.createdAt = submission.getCreatedAt();
        this.formType = submission.getFormType();
    }

    public Integer getSubmissionId() {
        return submissionId;
    }

    public Integer getMachineId(){
        return this.machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public String getItem() {
        return item;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public Integer getOperationId() {
        return operationId;
    }

    public String getCycleTime() {
        return cycleTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getFormType() {
        return formType;
    }
}
