package com.project.bookingService.config;

import com.project.bookingService.booking.Booking;
import com.project.bookingService.booking.BookingRepository;
import com.project.bookingService.organization.Organization;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

@Component
public class RandomDataGenerator {

    private static final int BOOKING_COUNT = 64;
    private static final int BOOKINGS_PER_DAY = 8;

    private static final String[] FIRST_NAMES = {
            "Anna", "Ben", "Clara", "David", "Elena", "Felix", "Greta", "Hugo", "Iris", "Jonas"
    };
    private static final String[] LAST_NAMES = {
            "Bauer", "Fischer", "Gruber", "Huber", "Keller", "Mayer", "Schmidt", "Wagner", "Weber", "Wolf"
            ,"Quintus Pompeius Senecio Sosius Priscus Quintus Pompeius Senecio Roscius Murena Coelius Sextus Iulius Frontinus Silius Decianus Gaius Iulius Eurycles Herculaneus Lucius Vibullius Pius Augustanus Alpinus Bellicius Sollers Iulius Aper Ducenius Proculus Rutilianus Rufinus Silius Valens Valerius Niger Claudius Fuscus Saxa Amyntianus Sosius Priscus"
    };
    private static final String[] SERVICES = {
            "Consultation", "Planning session", "Standard appointment", "Premium appointment", "Follow-up"
            ,"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    };
    private static final int[] DURATIONS = {30, 45, 60};

    private static final String emailForAll = "something@gmail.com";

    private final BookingRepository bookingRepository;
    private final RandomGenerator random = RandomGenerator.getDefault();

    public RandomDataGenerator(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public void createBookings(Organization organization) {
        List<Booking> bookings = new ArrayList<>(BOOKING_COUNT);
        LocalDate firstBookingDate = LocalDate.now().plusDays(3);

        for (int index = 0; index < BOOKING_COUNT; index++) {
            int dayOffset = index / BOOKINGS_PER_DAY;
            int slotIndex = index % BOOKINGS_PER_DAY;

            Booking booking = new Booking();
            booking.setOrganization(organization);
            booking.setClientFirstName(randomElement(FIRST_NAMES));
            booking.setClientLastName(randomElement(LAST_NAMES));
            booking.setBookingServicesToBeProvided(randomElement(SERVICES));
            booking.setBookingDurationInMinutes(randomElement());
            booking.setBookingIsPaid(random.nextBoolean());
            booking.setBookingDateAndTime(
                    firstBookingDate.plusDays(dayOffset).atTime(8 + (slotIndex * 2), 0)
            );
            booking.setAssociatedEmailAddress(emailForAll);
            bookings.add(booking);
        }
        bookingRepository.saveAll(bookings);
    }

    private String randomElement(String[] values) {
        return values[random.nextInt(values.length)];
    }

    private int randomElement() {
        return RandomDataGenerator.DURATIONS[random.nextInt(RandomDataGenerator.DURATIONS.length)];
    }
}
