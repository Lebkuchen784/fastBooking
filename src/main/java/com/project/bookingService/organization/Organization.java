package com.project.bookingService.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.bookingService.booking.Booking;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Booking> Bookings = new ArrayList<>();

    @Column(name="name")
    private String businessName;

    @Column(name="description", columnDefinition="TEXT")
    private String businessDescription;

    @Column(name="number_of_employees")
    private Integer businessNumberOfEmployees;

    @Column(name="address", columnDefinition="TEXT")
    private String businessAddress;

    @Column(name="opening_hour")
    private Timestamp businessOpeningHour;

    @Column(name="closing_hour")
    private Timestamp businessClosingHour;
}
