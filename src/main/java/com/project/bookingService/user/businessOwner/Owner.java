package com.project.bookingService.user.businessOwner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.bookingService.organization.Organization;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="owner")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String ID;

    // Not necessary
    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name="payment_method")
    private PaymentMethod paymentMethod;

    @JsonIgnore
    @Column(name="password_hash")
    private String passwordHash;

    @Column(name="email_address", unique = true)
    private String emailAddress;

    @Column(name="account_status")
    private Boolean accountStatus; // True = active, false = inactive

    @Column(name="type_of_business", columnDefinition="TEXT")
    private String typeOfBusiness; // Business description

    @Column(name="address", columnDefinition="TEXT")
    private String businessAddress;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "organization_id")
    @JsonManagedReference("owner-organization")
    private Organization organization;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Owner owner)) return false;
        return Objects.equals(ID, owner.ID);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + ", " + this.emailAddress;
    }
}
