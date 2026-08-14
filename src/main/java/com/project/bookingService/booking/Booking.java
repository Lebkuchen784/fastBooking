package com.project.bookingService.booking;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.bookingService.organization.Organization;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="organization_id")
    @JsonBackReference("organization-bookings")
    private Organization organization;

    @Column(name="is_paid")
    private Boolean bookingIsPaid;

    @Column(name="date_and_time")
    private LocalDateTime bookingDateAndTime;

    @Column(name="duration")
    private Integer bookingDurationInMinutes;

    @Column(name="services_provided", columnDefinition="TEXT")
    private String bookingServicesToBeProvided;

    @Column(name="client_first_name", columnDefinition="TEXT")
    private String clientFirstName;

    @Column(name="client_last_name", columnDefinition="TEXT")
    private String clientLastName;

    @Transient
    private Boolean errorFlag = false;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Booking booking)) return false;
        return Objects.equals(ID, booking.ID) &&
                Objects.equals(organization, booking.organization) &&
                Objects.equals(bookingDurationInMinutes, booking.bookingDurationInMinutes) &&
                Objects.equals(bookingDateAndTime, booking.bookingDateAndTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, organization, bookingDurationInMinutes, bookingDateAndTime);
    }
}
