package com.project.bookingService.booking;

public record BookingCreationDTO(
        Boolean bookingIsPaid,
        Integer bookingDurationInMinutes,
        String bookingDateAndTime,
        String bookingServicesProvided,
        String clientFirstName,
        String clientLastName,
        String associatedEmailAddress) {
}
