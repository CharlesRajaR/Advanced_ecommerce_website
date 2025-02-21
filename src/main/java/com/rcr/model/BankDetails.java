package com.rcr.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class BankDetails {
    private String AccountNumber;
    private String AccountHolderName;
    private String IFSC_Code;
}
