package com.rcr.serviceimpl;

import com.rcr.configuration.JwtTokenProvider;
import com.rcr.domain.AccountStatus;
import com.rcr.domain.UserRole;
import com.rcr.model.Address;
import com.rcr.model.Seller;
import com.rcr.model.VerificationCode;
import com.rcr.repository.AddressRepository;
import com.rcr.repository.SellerRepository;
import com.rcr.repository.VerificationCodeRepository;
import com.rcr.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final JwtTokenProvider jwtTokenProvider;
    private final SellerRepository sellerRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AddressRepository addressRepository;
    @Override
    public Seller getSellerProfile(String jwt) {
        String email = jwtTokenProvider.findEmailByJwtToken(jwt);

        Seller seller = sellerRepository.findByEmail(email);

        return seller;
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {
        if(sellerRepository.findByEmail(seller.getEmail()) != null){
            throw new Exception("seller already exists..");
        }
        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setGst(seller.getGst());
        newSeller.setName(seller.getName());
        newSeller.setRole(UserRole.ROLE_SELLER);
        newSeller.setPassword(seller.getPassword());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());
        if(addressRepository.findByDoorNo(seller.getPickupAddress().getDoorNo()) == null){
            Address address = new Address();
            address.setCity(seller.getPickupAddress().getCity());
            address.setCountry(seller.getPickupAddress().getCountry());
            address.setState(seller.getPickupAddress().getState());
            address.setLocality(seller.getPickupAddress().getLocality());
            address.setDoorNo(seller.getPickupAddress().getDoorNo());
            address.setPinCode(seller.getPickupAddress().getPinCode());
            address.setStreet_name(seller.getPickupAddress().getStreet_name());
            newSeller.setPickupAddress(addressRepository.save(address));
        }
        else{
            newSeller.setPickupAddress(addressRepository.findByDoorNo(seller.getPickupAddress().getDoorNo()));
        }

        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id)throws Exception {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new Exception("seller not found..."));

        return seller;
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
        Seller seller = sellerRepository.findByEmail(email);
        if(seller == null){
            throw new Exception("seller not found in this email");
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) {

        return sellerRepository.findByAccountStatus(status);
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {

        Seller old = getSellerById(id);

        if(seller.getGst() != null){
            old.setGst(seller.getGst());
        }
        if(seller.getRole() != null){
            old.setRole(seller.getRole());
        }
        if(seller.getBusinessDetails() != null
                && seller.getBusinessDetails().getBusinessAddress() != null
                && seller.getBusinessDetails().getBusinessEmail() != null
                && seller.getBusinessDetails().getBusinessMobile() != null
                && seller.getBusinessDetails().getBusinessName() != null){
            old.setBusinessDetails(seller.getBusinessDetails());
        }
        if(seller.getBankDetails() != null
        && seller.getBankDetails().getAccountNumber() != null
        && seller.getBankDetails().getIFSC_Code() != null
        && seller.getBankDetails().getAccountHolderName() != null){
            old.setBankDetails(seller.getBankDetails());
        }
        if(seller.getAccountStatus() != null){
            old.setAccountStatus(seller.getAccountStatus());
        }
        if(seller.getName() != null){
            old.setName(seller.getName());
        }
        if(seller.getPassword() != null){
            old.setPassword(seller.getPassword());
        }
        if(seller.getPickupAddress() != null){
            old.setPickupAddress(seller.getPickupAddress());
        }

        return sellerRepository.save(old);
    }

    @Override
    public void deleteSeller(Long id) throws Exception {
        Seller seller = getSellerById(id);

        sellerRepository.deleteById(id);
    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);

        if(verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw new Exception("invalid otp...");
        }

        Seller seller = getSellerByEmail(email);

        seller.setEmailVerified(true);
        seller.setAccountStatus(AccountStatus.ACTIVE);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(AccountStatus status, Long sellerId) throws Exception {
        Seller seller = getSellerById(sellerId);
        seller.setAccountStatus(status);
        return sellerRepository.save(seller);
    }
}
