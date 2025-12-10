package com.rfp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rfp_vendor")
public class RfpVendorEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long rfpId;
    private Long vendorId;

    // getters/setters
    public Long getId(){ 
        return id;
    }
    public void setId(Long id){ 
        this.id = id;
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

