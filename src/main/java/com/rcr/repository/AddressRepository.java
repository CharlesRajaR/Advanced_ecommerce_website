package com.rcr.repository;

import com.rcr.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    public Address findByDoorNo(String doorNo);
}
