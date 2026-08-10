package com.project.bookingService.organization;

import lombok.Data;

@Data
public class OrganizationCreationDTO {
    private final String businessName;
    private final String businessDescription;
    private final Integer businessNumberOfEmployees;
    private final String businessAddress;
    private final String businessOpeningHour;
    private final String businessClosingHour;
}
