package com.project.bookingService;

import com.project.bookingService.booking.Booking;
import com.project.bookingService.organization.OrganizationService;
import com.project.bookingService.user.businessOwner.OwnerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@AllArgsConstructor
public class WebController {

    private OwnerService ownerService;
    private OrganizationService organizationService;

    private static final int BOOKINGS_PER_PAGE = 12; // pagination

    @GetMapping("/owners/{ownerId}")
    ModelAndView index(@PathVariable String ownerId) {
        ModelAndView mav = new ModelAndView("index");

        if (ownerService.getNumberOfOwners() != 0 && organizationService.getNumberOfOrganizations() != 0) {
            mav.addObject("owner", ownerService.getOwner(ownerId));
            mav.addObject("organization", organizationService.getOrganizationByOwnerId(ownerId));
        }

        if (organizationService.getNumberOfBookings() != 0) {
            String organizationId = organizationService.getOrganizationIdByOwnerId(ownerId);
            if (organizationId != null) {
                Pageable pageable = PageRequest.of(0, 9, Sort.by(Sort.Direction.ASC, "bookingDateAndTime"));
                Page<Booking> recentBookingsPage = organizationService.getOrganizationBookingsByOwnerId(ownerId, pageable);
                if (recentBookingsPage.hasContent()) {
                    mav.addObject("bookings", recentBookingsPage.getContent());
                }
            }
        }

        return mav;
    }

    @GetMapping("/owners/{ownerId}/all-bookings")
    ModelAndView allBookings(@PathVariable String ownerId, @RequestParam(name = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), BOOKINGS_PER_PAGE, Sort.by(Sort.Direction.ASC, "bookingDateAndTime"));
        Page<Booking> bookingsPage = organizationService.getOrganizationBookingsByOwnerId(ownerId, pageable);

        ModelAndView mav = new ModelAndView("all-bookings");

        mav.addObject("owner", ownerService.getOwner(ownerId));
        mav.addObject("organization", organizationService.getOrganizationByOwnerId(ownerId));
        mav.addObject("bookingsPage", bookingsPage);

        return mav;
    }
}
