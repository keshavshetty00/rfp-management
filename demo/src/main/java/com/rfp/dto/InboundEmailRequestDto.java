package com.rfp.dto;

import jakarta.validation.constraints.NotBlank;

public class InboundEmailRequestDto {
    @NotBlank
    private String from;
    @NotBlank
    private String subject;
    @NotBlank
    private String body;

    private Long rfpId;     
    private Long vendorId;  

   
    public String getFrom(){ 
        return from;
    }
    public void setFrom(String from){
        this.from = from;
    }
    public String getSubject(){ 
        return subject;
    }
    public void setSubject(String subject){ 
        this.subject = subject;
    }
    public String getBody(){ 
        return body;
    }
    public void setBody(String body){
        this.body = body;
    }
    public Long getRfpId(){
        return rfpId;
    }
    public void setRfpId(Long rfpId){
        this.rfpId = rfpId;
    }
    public Long getVendorId(){
        return vendorId;
    }
    public void setVendorId(Long vendorId){
        this.vendorId = vendorId;
    }
}

