package com.project.bookingService.booking;

import com.project.bookingService.organization.Organization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne
    @JoinColumn(name="organization_id")
    private Organization organization;

    @Column(name="is_paid")
    private Boolean bookingIsPaid;

    @Column(name="duration")
    private Integer bookingDurationInMinutes;

    @Column(name="date_and_time")
    private LocalDateTime bookingDateAndTime;

    @Column(name="services_provided", columnDefinition="TEXT")
    private String bookingServicesToBeProvided;

    @Column(name="client_first_name")
    private String bookingClientFirstName;

    @Column(name="client_last_name")
    private Timestamp bookingClientLastName;
}
