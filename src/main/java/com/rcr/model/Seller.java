package com.rcr.model;

import com.rcr.domain.AccountStatus;
import com.rcr.domain.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    @Embedded
    private BusinessDetails businessDetails = new BusinessDetails();
    @Embedded
    private BankDetails bankDetails = new BankDetails();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pickup_address_id")
    private Address pickupAddress = new Address();
    private String gst;
    private UserRole role = UserRole.ROLE_SELLER;
    private boolean isEmailVerified = false;
    private AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;
}
