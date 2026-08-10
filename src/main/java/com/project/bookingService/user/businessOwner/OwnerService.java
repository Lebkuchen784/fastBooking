package com.project.bookingService.user.businessOwner;

import com.project.bookingService.organization.Organization;
import com.project.bookingService.organization.OrganizationCreationDTO;
import com.project.bookingService.organization.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Optional;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final OrganizationRepository organizationRepository;

    public OwnerService(OwnerRepository ownerRepository, OrganizationRepository organizationRepository) {
        this.ownerRepository = ownerRepository;
        this.organizationRepository = organizationRepository;
    }

    public Long getNumberOfOwners() {
        return ownerRepository.count();
    }

    @Transactional
    public Owner registerOwner(OwnerRegistrationDTO requestObject) {
        if (ownerRepository.existsByEmailAddress(requestObject.getEmailAddress())) {
            System.out.println("Account already exists for email: " + requestObject.getEmailAddress());
            return null;
        }

        Owner newOwner = new Owner();

        newOwner.setFirstName(requestObject.getFirstName());
        newOwner.setLastName(requestObject.getLastName());
        newOwner.setEmailAddress(requestObject.getEmailAddress());
        if (requestObject.getPaymentMethod() == null || !requestObject.getPaymentMethod().getDeclaringClass().isEnum()) {
            System.out.println("Invalid payment method.");
            return null;
        }
        newOwner.setPaymentMethod(requestObject.getPaymentMethod());
        newOwner.setPasswordHash(requestObject.getPassword());
        newOwner.setAccountStatus(requestObject.getAccountStatus());
        newOwner.setTypeOfBusiness(requestObject.getTypeOfBusiness());
        newOwner.setBusinessAddress(requestObject.getOwnerBusinessAddress());

        return ownerRepository.save(newOwner);
    }

    @Transactional
    public void removeOwner(String ownerId) {
        ownerRepository.deleteById(ownerId);
    }

    public Owner getOwner(String ownerId) {
        Optional<Owner> ownerOptional = ownerRepository.findById(ownerId);
        if (ownerOptional.isEmpty()) {
            System.out.println("Owner not found by id.");
            return null;
        }
        return ownerOptional.get();
    }

    @Transactional
    public Owner updateOwner(String ownerId, OwnerRegistrationDTO requestObject) {
        Optional<Owner> ownerOptional = ownerRepository.findById(ownerId);
        if (ownerOptional.isEmpty()) {
            System.out.println("Owner not found by id.");
            return null;
        }

        Owner fetchedOwner = ownerOptional.get();

        fetchedOwner.setFirstName(requestObject.getFirstName());
        fetchedOwner.setLastName(requestObject.getLastName());
        fetchedOwner.setEmailAddress(requestObject.getEmailAddress());
        fetchedOwner.setPaymentMethod(requestObject.getPaymentMethod());
        fetchedOwner.setPasswordHash(requestObject.getPassword());
        fetchedOwner.setAccountStatus(requestObject.getAccountStatus());
        fetchedOwner.setTypeOfBusiness(requestObject.getTypeOfBusiness());
        fetchedOwner.setBusinessAddress(requestObject.getOwnerBusinessAddress());

        return ownerRepository.save(fetchedOwner);
    }

    @Transactional
    public Organization updateOwnerOrganization(String ownerId, OrganizationCreationDTO requestBody) {
        Optional<Owner> ownerOptional = ownerRepository.findById(ownerId);
        if (ownerOptional.isEmpty()) {
            System.out.println("Owner not found by id.");
            return null;
        }

        Organization newOrg = new Organization();

        newOrg.setBusinessName(requestBody.getBusinessName());
        newOrg.setBusinessDescription(requestBody.getBusinessDescription());
        newOrg.setBusinessNumberOfEmployees(requestBody.getBusinessNumberOfEmployees());
        newOrg.setBusinessAddress(requestBody.getBusinessAddress());
        newOrg.setBusinessOpeningHour(LocalTime.parse(requestBody.getBusinessOpeningHour()));
        newOrg.setBusinessClosingHour(LocalTime.parse(requestBody.getBusinessClosingHour()));

        Owner fetchedOwner = ownerOptional.get();
        fetchedOwner.setOrganization(newOrg);
        ownerRepository.save(fetchedOwner);
        return fetchedOwner.getOrganization();
    }

    public Organization getOwnerOrganization(String ownerId) {
        if (ownerId == null) {
            System.out.println("Owner id is null.");
            return null;
        }

        Optional<Owner> optionalOwner = ownerRepository.findById(ownerId);
        if (optionalOwner.isEmpty()) {
            System.out.println("Owner not found by id.");
            return null;
        }

        return optionalOwner.get().getOrganization();
    }

    @Transactional
    public Owner removeOrgFromOwner(String ownerId) {
        Optional<Owner> ownerOptional = ownerRepository.findById(ownerId);
        if (ownerOptional.isEmpty()) {
            System.out.println("Owner not found by id.");
            return null;
        }

        Owner fetchedOwner = ownerOptional.get();
        fetchedOwner.setOrganization(null);
        return ownerRepository.save(fetchedOwner);
    }
}
