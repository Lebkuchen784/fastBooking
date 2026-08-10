package com.project.bookingService.user.businessOwner;

import lombok.Data;

@Data
public class OwnerRegistrationDTO {
    private final String firstName;
    private final String lastName;
    private final PaymentMethod paymentMethod;
    private final String emailAddress;
    private final String password;
    private final String typeOfBusiness;
    private final String ownerBusinessAddress;
    private final Boolean accountStatus;
}
