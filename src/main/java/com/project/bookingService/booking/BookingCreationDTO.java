package com.project.bookingService.booking;

import lombok.Data;

@Data
public class BookingCreationDTO {
    private final Boolean bookingIsPaid;
    private final Integer bookingDurationInMinutes;
    private final String bookingDateAndTime;
    private final String bookingServicesProvided;
    private final String clientFirstName;
    private final String clientLastName;
}
