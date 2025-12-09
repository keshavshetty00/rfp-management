package com.rfp.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class SendRfpRequest {
    @NotNull
    private Long rfpId;
    @NotNull
    private List<Long> vendorIds;

    public Long getRfpId(){ return rfpId;}
    public void setRfpId(Long rfpId){ this.rfpId = rfpId;}
    public List<Long> getVendorIds(){ return vendorIds;}
    public void setVendorIds(List<Long> vendorIds){ this.vendorIds = vendorIds;}
}
