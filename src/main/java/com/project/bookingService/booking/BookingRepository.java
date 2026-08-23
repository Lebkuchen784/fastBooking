package com.project.bookingService.booking;

import com.project.bookingService.organization.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @NativeQuery("SELECT CASE WHEN COUNT(b)>0 THEN TRUE ELSE FALSE END AS at_least_one_overlap_found FROM BOOKING b WHERE b.organization_id = :organizationId AND (b.date_and_time < :bookingDateAndTimeEnd) AND ((b.date_and_time + (b.duration * interval '1 minute')) > (:bookingDateAndTime))")
    Boolean bookingOverlapping(String organizationId, LocalDateTime bookingDateAndTime, LocalDateTime bookingDateAndTimeEnd);

    Page<Booking> findByOrganization(Organization organization, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.organization = :org AND (LOWER(b.clientFirstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(b.clientLastName) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<Booking> findByNameContainingAndByOrg(@Param("org") Organization org, @Param("name") String name, Pageable pageable);
}
