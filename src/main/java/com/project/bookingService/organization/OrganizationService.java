package com.project.bookingService.organization;

import com.project.bookingService.booking.Booking;
import com.project.bookingService.booking.BookingCreationDTO;
import com.project.bookingService.booking.BookingRepository;
import com.project.bookingService.user.businessOwner.Owner;
import com.project.bookingService.user.businessOwner.OwnerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OwnerRepository ownerRepository;
    private final BookingRepository bookingRepository;

    public OrganizationService(OrganizationRepository organizationRepository, OwnerRepository ownerRepository, BookingRepository bookingRepository) {
        this.organizationRepository = organizationRepository;
        this.ownerRepository = ownerRepository;
        this.bookingRepository = bookingRepository;
    }

    public Page<Booking> getOrganizationBookingsByOwnerId(String ownerId, Pageable pageable) {
        Optional<Owner> owner = ownerRepository.findById(ownerId);

        if (owner.isEmpty() || owner.get().getOrganization() == null) {
            System.out.println("Owner not found or does not have an organization registered.");
            return Page.empty(pageable);
        }

        return bookingRepository.findByOrganization(owner.get().getOrganization(), pageable);
    }

    public Long getNumberOfOrganizations() {
        return organizationRepository.count();
    }

    public String getOrganizationIdByOwnerId(String ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException("Owner ID cannot be null.");
        }

        Optional<Owner> owner = ownerRepository.findById(ownerId);

        if (owner.isEmpty()) {
            System.out.println("The requested owner does not exist in the database.");
            return null;
        }

        Owner fetchedOwner = owner.get();

        if (fetchedOwner.getOrganization() == null) {
            System.out.println("The requested owner does not have an organization registered.");
            return null;
        }

        Optional<Organization> organization = organizationRepository.findById(fetchedOwner.getOrganization().getID());
        if (organization.isEmpty()) {
            System.out.println("The requested organization does not exist in the database for the specified owner.");
            return null;
        }

        return organization.get().getID();
    }

    public Optional<Organization> getOrganizationById(String orgId) {
        if (orgId == null) {
            System.out.println("Organization ID was null, getOrganizationById returns Optional empty.");
            return Optional.empty();
        }

        return this.organizationRepository.findById(orgId);
    }

    public Long getNumberOfBookings() {
        return bookingRepository.count();
    }

    public Organization getOrganizationByOwnerId(String ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException("Owner ID cannot be null.");
        }

        Optional<Owner> owner = ownerRepository.findById(ownerId);
        if (owner.isEmpty()) {
            throw new IllegalArgumentException("Owner not found: " + ownerId);
        }

        Owner fetchedOwner = owner.get();

        Optional<Organization> org = organizationRepository.findById(fetchedOwner.getOrganization().getID());
        if (org.isEmpty()) {
            return null;
        }
        return fetchedOwner.getOrganization();
    }

    public Set<Booking> getOrganizationBookings(String orgId) {
        if (orgId == null) {
            throw new IllegalArgumentException("Organization ID cannot be null.");
        }

        Optional<Organization> org = organizationRepository.findById(orgId);
        if (org.isEmpty()) {
            return null;
        }

        Organization fetchedOrg = org.get();

        Set<Booking> bookings = fetchedOrg.getBookings();
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }

        return bookings;
    }

    @Transactional
    public Organization createOrganization(OrganizationCreationDTO requestObject, String ownerId) {
        if (requestObject == null || ownerId == null) {
            throw new IllegalArgumentException("Organization and/or owner ID cannot be null.");
        }

        Optional<Owner> owner = ownerRepository.findById(ownerId);
        if (owner.isEmpty()) {
            throw new IllegalArgumentException("Owner not found: " + ownerId);
        }

        Owner orgOwner = owner.get();
        Organization newOrganization = new Organization();

        newOrganization.setBookings(new HashSet<>());
        newOrganization.setOwner(orgOwner);
        newOrganization.setBusinessName(requestObject.getBusinessName());
        newOrganization.setBusinessDescription(requestObject.getBusinessDescription());
        newOrganization.setBusinessNumberOfEmployees(requestObject.getBusinessNumberOfEmployees());
        newOrganization.setBusinessAddress(requestObject.getBusinessAddress());
        newOrganization.setBusinessOpeningHour(LocalTime.parse(requestObject.getBusinessOpeningHour()));
        newOrganization.setBusinessClosingHour(LocalTime.parse(requestObject.getBusinessClosingHour()));

        orgOwner.setOrganization(newOrganization);
        return organizationRepository.save(newOrganization);
    }

    @Transactional
    public void deleteOrganizationFromOwner(String ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException("Owner ID cannot be null.");
        }

        Optional<Owner> owner = ownerRepository.findById(ownerId);
        if (owner.isEmpty()) {
            throw new IllegalArgumentException("Owner not found: " + ownerId);
        }

        Owner orgOwner = owner.get();
        orgOwner.setOrganization(null);
        ownerRepository.save(orgOwner);
    }

    @Transactional
    public Organization updateOrganization(String owner_id, OrganizationCreationDTO requestBody) {
        if (owner_id == null || requestBody == null) {
            throw new IllegalArgumentException("Owner ID and/or organization instance cannot be null.");
        }

        Optional<Owner> owner = ownerRepository.findById(owner_id);
        if (owner.isEmpty()) {
            throw new IllegalArgumentException("Owner not found: " + owner_id);
        }

        Owner orgOwner = owner.get();
        Organization org = orgOwner.getOrganization();

        org.setBusinessName(requestBody.getBusinessName());
        org.setBusinessDescription(requestBody.getBusinessDescription());
        org.setBusinessNumberOfEmployees(requestBody.getBusinessNumberOfEmployees());
        org.setBusinessAddress(requestBody.getBusinessAddress());
        org.setBusinessOpeningHour(LocalTime.parse(requestBody.getBusinessOpeningHour()));
        org.setBusinessClosingHour(LocalTime.parse(requestBody.getBusinessClosingHour()));

        orgOwner.setOrganization(org);
        return organizationRepository.save(org);
    }

    @Transactional
    public Booking addBookingToOrganization(String organizationId, BookingCreationDTO requestObject) {
        if (organizationId == null || requestObject == null) {
            throw new IllegalArgumentException("Organization ID and/or booking instance cannot be null.");
        }

        Booking errorFeedbackObject = new Booking();
        errorFeedbackObject.setErrorFlag(true);

        Optional<Organization> organizationOptional = organizationRepository.findById(organizationId);
        if (organizationOptional.isEmpty()) {
            System.out.println("Organization not found in the repo: " + organizationId);
            return errorFeedbackObject;
        }

        Organization organization = organizationOptional.get();

        LocalDateTime bookingDateAndTime = LocalDateTime.parse(requestObject.bookingDateAndTime());
        LocalDateTime bookingEndTime = bookingDateAndTime.plusMinutes(requestObject.bookingDurationInMinutes());

        if (bookingEndTime.isAfter(organization.getBusinessClosingHour().atDate(bookingEndTime.toLocalDate()))) {
            System.out.println("Booking cannot be scheduled after the organization's closing hours: " + organization.getBusinessClosingHour() + ".");
            return errorFeedbackObject;
        }

        if (bookingDateAndTime.isBefore(organization.getBusinessOpeningHour().atDate(bookingDateAndTime.toLocalDate()))) {
            System.out.println("Booking cannot be scheduled before the organization's opening hours: " + organization.getBusinessOpeningHour() + ".");
            return errorFeedbackObject;
        }

        if (bookingRepository.bookingOverlapping(organizationId, bookingDateAndTime, bookingEndTime)) {
            System.out.println("Overlapping found in bookings, the new booking cannot be scheduled when there is a time conflict between the existing bookings.");
            return errorFeedbackObject;
        }

        Booking newBooking = new Booking();

        newBooking.setOrganization(organization);
        newBooking.setBookingIsPaid(requestObject.bookingIsPaid());
        newBooking.setBookingDurationInMinutes(requestObject.bookingDurationInMinutes());
        newBooking.setBookingDateAndTime(LocalDateTime.parse(requestObject.bookingDateAndTime()));
        newBooking.setBookingServicesToBeProvided(requestObject.bookingServicesProvided());
        newBooking.setClientFirstName(requestObject.clientFirstName());
        newBooking.setClientLastName(requestObject.clientLastName());

        organization.getBookings().add(newBooking);
        organizationRepository.save(organization);
        return newBooking;
    }
}
