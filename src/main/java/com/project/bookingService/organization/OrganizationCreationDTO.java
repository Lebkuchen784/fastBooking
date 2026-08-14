package com.project.bookingService.organization;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationCreationDTO {
    private String businessName;
    private String businessDescription;
    private Integer businessNumberOfEmployees;
    private String businessAddress;
    private String businessOpeningHour;
    private String businessClosingHour;
}
