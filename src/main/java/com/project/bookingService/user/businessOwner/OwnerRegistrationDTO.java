package com.project.bookingService.user.businessOwner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerRegistrationDTO {
    private String firstName;
    private String lastName;
    private PaymentMethod paymentMethod;
    private String emailAddress;
    private String password;
    private String typeOfBusiness;
    private String ownerBusinessAddress;
    private Boolean accountStatus;
}
