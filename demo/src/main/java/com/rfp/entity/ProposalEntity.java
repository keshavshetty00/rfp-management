package com.rfp.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "proposal")
public class ProposalEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long rfpId;
    private Long vendorId;

    private Double price;
    private Integer deliveryDays;
    private String paymentTerms;
    private String warranty;

    @Column(columnDefinition = "TEXT")
    private String rawResponse;

    private Instant parsedAt;

    // getters/setters
    // ...
    public Long getId(){ return id;}
    public void setId(Long id){ this.id = id;}
    public Long getRfpId(){ return rfpId;}
    public void setRfpId(Long rfpId){ this.rfpId = rfpId;}
    public Long getVendorId(){ return vendorId;}
    public void setVendorId(Long vendorId){ this.vendorId = vendorId;}
    public Double getPrice(){ return price;}
    public void setPrice(Double price){ this.price = price;}
    public Integer getDeliveryDays(){ return deliveryDays;}
    public void setDeliveryDays(Integer deliveryDays){ this.deliveryDays = deliveryDays;}
    public String getPaymentTerms(){ return paymentTerms;}
    public void setPaymentTerms(String paymentTerms){ this.paymentTerms = paymentTerms;}
    public String getWarranty(){ return warranty;}
    public void setWarranty(String warranty){ this.warranty = warranty;}
    public String getRawResponse(){ return rawResponse;}
    public void setRawResponse(String rawResponse){ this.rawResponse = rawResponse;}
    public Instant getParsedAt(){ return parsedAt;}
    public void setParsedAt(Instant parsedAt){ this.parsedAt = parsedAt;}
}
