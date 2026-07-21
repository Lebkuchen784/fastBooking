package com.project.bookingService.user.businessOwner;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="owner")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Enumerated()
    @Column(name="payment_method")
    private PaymentMethod paymentMethod;

    @Column(name="password_hash")
    private String passwordHash;

    @Column(name="email_address")
    private String emailAddress;

    @Column(name="account_status")
    private Boolean accountStatus; // True = active, false = inactive

    @Column(name="type_of_business")
    private String typeOfBusiness; // Business description

    @Column(name="address")
    private String businessAddress;
}
