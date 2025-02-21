package com.rcr.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class BusinessDetails {
    private String businessName;
    private String businessEmail;
    private String businessMobile;
    private String businessAddress;
    private String logo;
    private String banner;
}
