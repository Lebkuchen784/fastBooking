package com.project.bookingService.organization;

import com.project.bookingService.booking.Booking;
import com.project.bookingService.booking.BookingCreationDTO;
import com.project.bookingService.booking.BookingRepository;
import com.project.bookingService.config.email_service.EmailSender;
import com.project.bookingService.user.businessOwner.Owner;
import com.project.bookingService.user.businessOwner.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private EmailSender sender;

    @InjectMocks
    private OrganizationService organizationService;

    private Owner mockOwner;
    private Organization mockOrganization;
    private Booking mockBooking;
    private OrganizationCreationDTO orgCreationDTO;
    private BookingCreationDTO bookingCreationDTO;

    @BeforeEach
    void setup() {
        mockOrganization = new Organization();
        mockOrganization.setID("org-123");
        mockOrganization.setBusinessName("Test Org");
        mockOrganization.setBusinessOpeningHour(LocalTime.parse("09:00"));
        mockOrganization.setBusinessClosingHour(LocalTime.parse("17:00"));
        mockOrganization.setBookings(new HashSet<>());

        mockOwner = new Owner();
        mockOwner.setID("owner-123");
        mockOwner.setOrganization(mockOrganization);
        mockOrganization.setOwner(mockOwner);

        mockBooking = new Booking();
        mockBooking.setID("booking-123");
        mockBooking.setOrganization(mockOrganization);
        mockBooking.setBookingDurationInMinutes(30);
        mockBooking.setBookingDateAndTime(LocalDateTime.parse("2026-08-18T10:00"));
        mockOrganization.getBookings().add(mockBooking);

        orgCreationDTO = new OrganizationCreationDTO();
        orgCreationDTO.setBusinessName("New Org");
        orgCreationDTO.setBusinessOpeningHour("08:00");
        orgCreationDTO.setBusinessClosingHour("18:00");

        bookingCreationDTO = new BookingCreationDTO(false, 30, "2026-08-18T10:00", "Haircut", "Joe", "Mama", "joe@mama.com", false);
    }

    @Test
    void getOrganizationBookingsByOwnerId_foundAndNotFound() {
        when(ownerRepository.findById("invalid-id")).thenReturn(Optional.empty());
        Page<Booking> emptyResult = organizationService.getOrganizationBookingsByOwnerId("invalid-id", Pageable.unpaged());
        assertTrue(emptyResult.isEmpty());

        when(ownerRepository.findById("owner-123")).thenReturn(Optional.of(mockOwner));
        when(bookingRepository.findByOrganization(eq(mockOrganization), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(mockBooking)));
        Page<Booking> result = organizationService.getOrganizationBookingsByOwnerId("owner-123", Pageable.unpaged());
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getNumberOfOrganizations_success() {
        when(organizationRepository.count()).thenReturn(5L);
        assertEquals(5L, organizationService.getNumberOfOrganizations());
    }

    @Test
    void getOrganizationIdByOwnerId_foundAndNotFound() {
        assertThrows(IllegalArgumentException.class, () -> organizationService.getOrganizationIdByOwnerId(null));

        when(ownerRepository.findById("invalid-id")).thenReturn(Optional.empty());
        assertNull(organizationService.getOrganizationIdByOwnerId("invalid-id"));

        Owner ownerWithoutOrg = new Owner();
        ownerWithoutOrg.setID("owner-456");
        when(ownerRepository.findById("owner-456")).thenReturn(Optional.of(ownerWithoutOrg));
        assertNull(organizationService.getOrganizationIdByOwnerId("owner-456"));

        when(ownerRepository.findById("owner-123")).thenReturn(Optional.of(mockOwner));
        when(organizationRepository.findById("org-123")).thenReturn(Optional.of(mockOrganization));
        assertEquals("org-123", organizationService.getOrganizationIdByOwnerId("owner-123"));
    }

    @Test
    void getOrganizationById_foundAndNotFound() {
        assertEquals(Optional.empty(), organizationService.getOrganizationById(null));

        when(organizationRepository.findById("org-123")).thenReturn(Optional.of(mockOrganization));
        assertTrue(organizationService.getOrganizationById("org-123").isPresent());
    }

    @Test
    void getNumberOfBookings_success() {
        when(bookingRepository.count()).thenReturn(10L);
        assertEquals(10L, organizationService.getNumberOfBookings());
    }

    @Test
    void getOrganizationByOwnerId_foundAndNotFound() {
        assertThrows(IllegalArgumentException.class, () -> organizationService.getOrganizationByOwnerId(null));
        assertThrows(IllegalArgumentException.class, () -> organizationService.getOrganizationByOwnerId("invalid-id"));

        when(ownerRepository.findById("owner-123")).thenReturn(Optional.of(mockOwner));
        when(organizationRepository.findById("org-123")).thenReturn(Optional.of(mockOrganization));
        assertNotNull(organizationService.getOrganizationByOwnerId("owner-123"));
    }

    @Test
    void getOrganizationBookings_foundAndNotFound() {
        assertThrows(IllegalArgumentException.class, () -> organizationService.getOrganizationBookings(null));

        when(organizationRepository.findById("invalid-id")).thenReturn(Optional.empty());
        assertNull(organizationService.getOrganizationBookings("invalid-id"));

        when(organizationRepository.findById("org-123")).thenReturn(Optional.of(mockOrganization));
        Set<Booking> result = organizationService.getOrganizationBookings("org-123");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void removeBookingFromOrg_successAndFailure() {
        assertFalse(organizationService.removeBookingFromOrg(null, null));

        when(organizationRepository.findById("org-123")).thenReturn(Optional.of(mockOrganization));
        when(bookingRepository.findById("booking-123")).thenReturn(Optional.of(mockBooking));
        
        assertTrue(organizationService.removeBookingFromOrg("org-123", "booking-123"));
        assertTrue(organizationService.getBookingById("booking-123").orElseThrow().getHasBeenCancelled());
    }

    @Test
    void getBookingById_foundAndNotFound() {
        assertEquals(Optional.empty(), organizationService.getBookingById(null));
        when(bookingRepository.findById("booking-123")).thenReturn(Optional.of(mockBooking));
        assertTrue(organizationService.getBookingById("booking-123").isPresent());
    }

    @Test
    void searchMethodByName_foundAndNotFound() {
        when(ownerRepository.findById("invalid-id")).thenReturn(Optional.empty());
        assertTrue(organizationService.searchMethodByName("invalid-id", "test", Pageable.unpaged()).isEmpty());

        when(ownerRepository.findById("owner-123")).thenReturn(Optional.of(mockOwner));
        when(bookingRepository.findByNameContainingAndByOrg(eq(mockOrganization), eq("Joe"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(mockBooking)));
        
        Page<Booking> result = organizationService.searchMethodByName("owner-123", "Joe", Pageable.unpaged());
        assertFalse(result.isEmpty());
    }

    @Test
    void createOrganization_successAndFailure() {
        assertThrows(IllegalArgumentException.class, () -> organizationService.createOrganization(null, null));
        assertThrows(IllegalArgumentException.class, () -> organizationService.createOrganization(orgCreationDTO, "invalid-id"));

        when(ownerRepository.findById("owner-123")).thenReturn(Optional.of(mockOwner));
        when(organizationRepository.save(any(Organization.class))).thenReturn(mockOrganization);

        Organization result = organizationService.createOrganization(orgCreationDTO, "owner-123");
        assertNotNull(result);
    }

    @Test
    void deleteOrganizationFromOwner_successAndFailure() {
        assertThrows(IllegalArgumentException.class, () -> organizationService.deleteOrganizationFromOwner(null));
        assertThrows(IllegalArgumentException.class, () -> organizationService.deleteOrganizationFromOwner("invalid-id"));

        when(ownerRepository.findById("owner-123")).thenReturn(Optional.of(mockOwner));
        organizationService.deleteOrganizationFromOwner("owner-123");
        verify(ownerRepository, times(1)).save(mockOwner);
        assertNull(mockOwner.getOrganization());
    }

    @Test
    void updateOrganization_successAndFailure() {
        assertThrows(IllegalArgumentException.class, () -> organizationService.updateOrganization(null, null));
        assertThrows(IllegalArgumentException.class, () -> organizationService.updateOrganization("invalid-id", orgCreationDTO));

        when(ownerRepository.findById("owner-123")).thenReturn(Optional.of(mockOwner));
        when(organizationRepository.save(any(Organization.class))).thenReturn(mockOrganization);

        Organization result = organizationService.updateOrganization("owner-123", orgCreationDTO);
        assertNotNull(result);
    }

    @Test
    void addBookingToOrganization_successAndFailure() {
        assertThrows(IllegalArgumentException.class, () -> organizationService.addBookingToOrganization(null, null));

        when(organizationRepository.findById("invalid-id")).thenReturn(Optional.empty());
        Booking notFoundResult = organizationService.addBookingToOrganization("invalid-id", bookingCreationDTO);
        assertTrue(notFoundResult.getErrorFlag());

        when(organizationRepository.findById("org-123")).thenReturn(Optional.of(mockOrganization));
        when(bookingRepository.bookingOverlapping(anyString(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Booking successResult = organizationService.addBookingToOrganization("org-123", bookingCreationDTO);
        assertNotNull(successResult);
        assertFalse(successResult.getErrorFlag());
        assertEquals("Joe", successResult.getClientFirstName());
    }
}
