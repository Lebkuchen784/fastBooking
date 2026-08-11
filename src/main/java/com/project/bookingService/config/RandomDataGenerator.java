package com.project.bookingService.config;

import com.project.bookingService.booking.Booking;
import com.project.bookingService.booking.BookingRepository;
import com.project.bookingService.organization.Organization;
import com.project.bookingService.user.businessOwner.Owner;
import com.project.bookingService.user.businessOwner.OwnerRepository;
import com.project.bookingService.user.businessOwner.PaymentMethod;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

@Component
@ConditionalOnProperty(name = "sample-data.enabled", havingValue = "true")
public class RandomDataGenerator implements ApplicationRunner {

    private static final int BOOKING_COUNT = 50;
    private static final int BOOKINGS_PER_DAY = 5;

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

    private final OwnerRepository ownerRepository;
    private final BookingRepository bookingRepository;
    private final RandomGenerator random = RandomGenerator.getDefault();

    public RandomDataGenerator(OwnerRepository ownerRepository, BookingRepository bookingRepository) {
        this.ownerRepository = ownerRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        if (ownerRepository.count() > 0) {
            return;
        }

        Organization organization = createOrganization();
        Owner owner = createOwner(organization);
        organization.setOwner(owner);
        ownerRepository.save(owner);

        List<Booking> bookings = createBookings(organization);
        bookingRepository.saveAll(bookings);

        System.out.println("Owner ID: " + owner.getID());
    }

    private Owner createOwner(Organization organization) {
        Owner owner = new Owner();
        owner.setFirstName("Joe");
        owner.setLastName("Mama");
        owner.setEmailAddress("email@example.com");
        owner.setPassword("1234");
        owner.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        owner.setAccountStatus(true);
        owner.setTypeOfBusiness("67 services");
        owner.setBusinessAddress("Straight from the underground");
        owner.setOrganization(organization);
        return owner;
    }

    private Organization createOrganization() {
        Organization organization = new Organization();
        organization.setBusinessName("Joe's Business");
        organization.setBusinessDescription("Providing great services for cheap prices");
        organization.setBusinessNumberOfEmployees(10);
        organization.setBusinessAddress("Somewhere over the rainbow");
        organization.setBusinessOpeningHour(LocalTime.of(8, 0));
        organization.setBusinessClosingHour(LocalTime.of(18, 0));
        return organization;
    }

    private List<Booking> createBookings(Organization organization) {
        List<Booking> bookings = new ArrayList<>(BOOKING_COUNT);
        LocalDate firstBookingDate = LocalDate.now().minusDays(12);

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
            bookings.add(booking);
        }

        return bookings;
    }

    private String randomElement(String[] values) {
        return values[random.nextInt(values.length)];
    }

    private int randomElement() {
        return RandomDataGenerator.DURATIONS[random.nextInt(RandomDataGenerator.DURATIONS.length)];
    }
}
