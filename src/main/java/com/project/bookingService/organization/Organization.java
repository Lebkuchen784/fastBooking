package com.project.bookingService.organization;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.bookingService.booking.Booking;
import com.project.bookingService.user.businessOwner.Owner;
import jakarta.persistence.*;
import lombok.*;

import java.nio.ByteBuffer;
import java.time.LocalTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="organization")
public class Organization {
    @Id
    private String ID;

    @PrePersist
    private void generateId() {
        if (this.ID == null) {
            UUID uuid = UUID.randomUUID();
            ByteBuffer byteBuffer = ByteBuffer.allocate(16);
            byteBuffer.putLong(uuid.getMostSignificantBits());
            byteBuffer.putLong(uuid.getLeastSignificantBits());
            this.ID = Base64.getUrlEncoder().withoutPadding().encodeToString(byteBuffer.array());
        }
    }

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("organization-bookings")
    private Set<Booking> Bookings = new HashSet<>();

    @OneToOne(mappedBy = "organization")
    @JsonBackReference("owner-organization")
    private Owner owner;

    @Column(name="name")
    private String businessName;

    @Column(name="description", columnDefinition="TEXT")
    private String businessDescription;

    @Column(name="number_of_employees")
    private Integer businessNumberOfEmployees;

    @Column(name="address", columnDefinition="TEXT")
    private String businessAddress;

    @Column(name="opening_hour")
    private LocalTime businessOpeningHour;

    @Column(name="closing_hour")
    private LocalTime businessClosingHour;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Organization organization)) return false;
        return Objects.equals(ID, organization.ID) && Objects.equals(owner, organization.owner);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
