package com.project.bookingService.user.businessOwner;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, String> {
    boolean existsByEmailAddress(String emailAddress);
    Optional<Owner> findByEmailAddress(String emailAddress);
}
