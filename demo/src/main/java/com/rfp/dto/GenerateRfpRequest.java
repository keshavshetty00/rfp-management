package com.rfp.dto;

import jakarta.validation.constraints.NotBlank;

public class GenerateRfpRequest {
    @NotBlank
    private String description;

    public String getDescription(){ return description;}
    public void setDescription(String description){ this.description = description;}
}
