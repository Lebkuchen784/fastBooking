package com.project.bookingService.booking;

import com.project.bookingService.organization.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @NativeQuery("SELECT CASE WHEN COUNT(b)>0 THEN TRUE ELSE FALSE END AS at_least_one_overlap_found FROM BOOKING b WHERE b.organization_id = :organizationId AND (b.date_and_time < :bookingDateAndTimeEnd) AND ((b.date_and_time + (b.duration * interval '1 minute')) > (:bookingDateAndTime))")
    Boolean bookingOverlapping(String organizationId, LocalDateTime bookingDateAndTime, LocalDateTime bookingDateAndTimeEnd);

    Page<Booking> findByOrganization(Organization organization, Pageable pageable);
}
