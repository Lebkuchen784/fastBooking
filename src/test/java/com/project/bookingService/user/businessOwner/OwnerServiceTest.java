package com.project.bookingService.user.businessOwner;

import com.project.bookingService.config.email_service.EmailSender;
import com.project.bookingService.organization.Organization;
import com.project.bookingService.organization.OrganizationCreationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailSender sender; // Without this the registerOwner method throws a null exception

    @InjectMocks
    private OwnerService ownerService;

    private Owner mockOwner;
    private OwnerRegistrationDTO DTO;

    @BeforeEach
    void setup() {
        mockOwner = new Owner();
        mockOwner.setID("123");
        mockOwner.setEmailAddress("test@67.com");
        mockOwner.setPassword("superSecret");
        mockOwner.setFirstName("John");
        mockOwner.setLastName("Smith");

        DTO = new OwnerRegistrationDTO();
        DTO.setFirstName("John");
        DTO.setLastName("Smith");
        DTO.setEmailAddress("test@67.com");
        DTO.setPassword("noHashingYet");
        DTO.setPaymentMethod(PaymentMethod.CREDIT_CARD);
    }

    @Test
    void loadUserByEmail_foundAndNotFound() {
        assertThrows(UsernameNotFoundException.class, () -> ownerService.loadUserByUsername("nothing"));

        when(ownerRepository.findByEmailAddress("test@67.com")).thenReturn(Optional.of(mockOwner));
        UserDetails userDetails = ownerService.loadUserByUsername("test@67.com");
        assertNotNull(userDetails);
        assertEquals("test@67.com", userDetails.getUsername());
        assertEquals("superSecret", userDetails.getPassword());
    }

    @Test
    void getOwnerByEmail_foundAndNotFound() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.getOwnerByEmail("101001"));

        when(ownerRepository.findByEmailAddress("test@67.com")).thenReturn(Optional.of(mockOwner));
        Owner result = ownerService.getOwnerByEmail("test@67.com");
        assertNotNull(result);
        assertEquals("test@67.com", result.getEmailAddress());
    }

    @Test
    void registerOwner_successAndFailure() {
        when(ownerRepository.existsByEmailAddress(DTO.getEmailAddress())).thenReturn(true);
        Owner failureResult = ownerService.registerOwner(DTO);
        assertNull(failureResult);

        when(ownerRepository.existsByEmailAddress(DTO.getEmailAddress())).thenReturn(false);
        DTO.setPaymentMethod(null);
        Owner nullPaymentResult = ownerService.registerOwner(DTO);
        assertNull(nullPaymentResult);

        DTO.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        when(passwordEncoder.encode("noHashingYet")).thenReturn("encodedPassword");
        when(ownerRepository.save(any(Owner.class))).thenReturn(mockOwner);

        Owner successResult = ownerService.registerOwner(DTO);
        assertNotNull(successResult);
        assertEquals("test@67.com", successResult.getEmailAddress());
    }

    @Test
    void removeOwner_success() {
        ownerService.removeOwner("123");
        verify(ownerRepository, times(1)).deleteById("123");
        verifyNoMoreInteractions(ownerRepository);
    }

    @Test
    void getOwner_foundAndNotFound() {
        when(ownerRepository.findById("invalid-id")).thenReturn(Optional.empty());
        assertNull(ownerService.getOwner("invalid-id"));

        when(ownerRepository.findById("123")).thenReturn(Optional.of(mockOwner));
        Owner result = ownerService.getOwner("123");
        assertNotNull(result);
        assertEquals("123", result.getID());
    }

    @Test
    void updateOwner_foundAndNotFound() {
        when(ownerRepository.findById("invalid-id")).thenReturn(Optional.empty());
        assertNull(ownerService.updateOwner("invalid-id", DTO));
        
        when(ownerRepository.findById("123")).thenReturn(Optional.of(mockOwner));
        when(ownerRepository.save(any(Owner.class))).thenReturn(mockOwner);

        DTO.setFirstName("Jane");
        Owner result = ownerService.updateOwner("123", DTO);

        assertNotNull(result);
        assertEquals("Jane", mockOwner.getFirstName());
    }

    @Test
    void updateOwnerOrganization_foundAndNotFound() {
        OrganizationCreationDTO orgDTO = new OrganizationCreationDTO();
        orgDTO.setBusinessName("Test Business");
        orgDTO.setBusinessOpeningHour("09:00");
        orgDTO.setBusinessClosingHour("17:00");

        when(ownerRepository.findById("invalid-id")).thenReturn(Optional.empty());
        assertNull(ownerService.updateOwnerOrganization("invalid-id", orgDTO));

        when(ownerRepository.findById("123")).thenReturn(Optional.of(mockOwner));
        Organization result = ownerService.updateOwnerOrganization("123", orgDTO);
        
        assertNotNull(result);
        assertEquals("Test Business", result.getBusinessName());
    }

    @Test
    void getOwnerOrganization_foundAndNotFound() {
        assertNull(ownerService.getOwnerOrganization(null));
        
        when(ownerRepository.findById("invalid-id")).thenReturn(Optional.empty());
        assertNull(ownerService.getOwnerOrganization("invalid-id"));

        Organization org = new Organization();
        org.setBusinessName("Test Business");
        mockOwner.setOrganization(org);
        
        when(ownerRepository.findById("123")).thenReturn(Optional.of(mockOwner));
        Organization result = ownerService.getOwnerOrganization("123");
        
        assertNotNull(result);
        assertEquals("Test Business", result.getBusinessName());
    }

    @Test
    void removeOrgFromOwner_foundAndNotFound() {
        when(ownerRepository.findById("invalid-id")).thenReturn(Optional.empty());
        assertNull(ownerService.removeOrgFromOwner("invalid-id"));

        Organization org = new Organization();
        mockOwner.setOrganization(org);
        
        when(ownerRepository.findById("123")).thenReturn(Optional.of(mockOwner));
        when(ownerRepository.save(any(Owner.class))).thenReturn(mockOwner);
        
        Owner result = ownerService.removeOrgFromOwner("123");
        assertNotNull(result);
        assertNull(result.getOrganization());
    }
}
