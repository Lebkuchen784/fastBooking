package com.project.bookingService.organization;

import com.project.bookingService.booking.Booking;
import com.project.bookingService.booking.BookingCreationDTO;
import com.project.bookingService.booking.RandomDataGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final RandomDataGenerator randomDataGenerator;

    public OrganizationController(OrganizationService organizationService, RandomDataGenerator randomDataGenerator) {
        this.organizationService = organizationService;
        this.randomDataGenerator = randomDataGenerator;
    }

    @PostMapping("/{owner_id}/generateMockBookings")
    public ResponseEntity<Void> generateTestBookings(@PathVariable String owner_id) {
        Organization result = organizationService.getOrganizationByOwnerId(owner_id);
        if (result != null) {
            randomDataGenerator.createBookings(result);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/{owner_id}")
    public ResponseEntity<Organization> createOrganization(
            @PathVariable String owner_id,
            @RequestBody OrganizationCreationDTO requestObject) {
        Organization result = organizationService.createOrganization(requestObject, owner_id);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/{owner_id}")
    public ResponseEntity<Void> deleteOrganizationFromOwner(
            @PathVariable String owner_id) {
        organizationService.deleteOrganizationFromOwner(owner_id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{owner_id}/updateOrg")
    public ResponseEntity<Organization> updateOrganizationDetails(
            @PathVariable String owner_id,
            @RequestBody OrganizationCreationDTO requestObject) {
        Organization result = organizationService.updateOrganization(owner_id, requestObject);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/{organizationId}/bookings")
    public ResponseEntity<Booking> addBookingToOrganization(
            @PathVariable String organizationId,
            @RequestBody BookingCreationDTO requestObject
    ) {
        Booking result = organizationService.addBookingToOrganization(organizationId, requestObject);
        if (result.getErrorFlag()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/{organizationId}/bookings/{bookingId}")
    public ResponseEntity<Booking> removeBookingFromOrg(
            @PathVariable String organizationId,
            @PathVariable String bookingId
    ) {
        if (organizationService.removeBookingFromOrg(organizationId, bookingId)) {
            return ResponseEntity.status(HttpStatus.FOUND).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{owner_id}")
    public ResponseEntity<Organization> getOrganizationByOwner(
            @PathVariable String owner_id) {
        Organization result = organizationService.getOrganizationByOwnerId(owner_id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{owner_id}/bookings")
    public ResponseEntity<Set<Booking>> getBookingsByOrgId(
            @PathVariable String owner_id
    ) {
        Organization result = organizationService.getOrganizationByOwnerId(owner_id);
        return result.getBookings() == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.status(HttpStatus.OK).body(result.getBookings());
    }
}
