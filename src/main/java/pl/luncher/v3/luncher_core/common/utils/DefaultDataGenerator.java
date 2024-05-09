package pl.luncher.v3.luncher_core.common.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.common.domain.OpeningWindow;
import pl.luncher.v3.luncher_core.common.domain.Place;
import pl.luncher.v3.luncher_core.common.domain.PlaceOpeningException;
import pl.luncher.v3.luncher_core.common.domain.PlaceType;
import pl.luncher.v3.luncher_core.common.domain.infra.AppRole;
import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.domain.valueobjects.Address;
import pl.luncher.v3.luncher_core.common.repositories.PlaceRepository;
import pl.luncher.v3.luncher_core.common.repositories.PlaceTypeRepository;
import pl.luncher.v3.luncher_core.common.repositories.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultDataGenerator {

  private final PlaceRepository placeRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final PlaceTypeRepository placeTypeRepository;


  @EventListener(ApplicationReadyEvent.class)
  public void seedData() {

    //USERS
    List<User> users = List.of(
        User.builder().firstName("Grzegorz").surname("Root").email("root@g.pl")
            .role(AppRole.SYS_ROOT).passwordHash(passwordEncoder.encode("1234")).enabled(true)
            .build(),
        User.builder().firstName("Grzegorz").surname("Admin").email("admin@g.pl")
            .role(AppRole.SYS_ADMIN).passwordHash(passwordEncoder.encode("1234")).enabled(true)
            .build(),
        User.builder().firstName("Grzegorz").surname("Mod").email("mod@g.pl")
            .role(AppRole.SYS_MOD).passwordHash(passwordEncoder.encode("1234")).enabled(true)
            .build(),
        User.builder().firstName("Grzegorz").surname("Manager").email("manager@g.pl")
            .role(AppRole.REST_MANAGER).enabled(true)
            .passwordHash(passwordEncoder.encode("1234")).build(),
        User.builder().firstName("Grzegorz").surname("Rest-User").email("employee@g.pl")
            .role(AppRole.REST_USER).passwordHash(passwordEncoder.encode("1234")).enabled(true)
            .build(),
        User.builder().firstName("Grzegorz").surname("End-User").email("user@g.pl")
            .role(AppRole.USER).passwordHash(passwordEncoder.encode("1234")).enabled(true)
            .build());

    for (User user : users) {
      if (userRepository.existsByEmail(user.getEmail())) {
        log.info("User {} already exists. Skipping.", user.getEmail());
        continue;
      }
      userRepository.saveAndFlush(user);
      log.info("Created user {} {} ({})", user.getFirstName(), user.getSurname(), user.getEmail());
    }

    User owner = userRepository.findUserByEmail("manager@g.pl").orElseThrow();
    User employee = userRepository.findUserByEmail("employee@g.pl").orElseThrow();

    //place types
    var restType = new PlaceType("RESTAURANT", "restaurant", "Restauracja");
    var barType = new PlaceType("BAR", "wine_bar", "Bar");
    var clubType = new PlaceType("CLUB", "nightlife", "Klub");

    placeTypeRepository.saveAll(List.of(restType, barType, clubType));

    //restaurants
    Place r1 = Place.builder().name("The Cool Cat").longName("The Cool Cat - TR").owner(owner)
        .allowedUsers(Set.of(employee))
        .address(Address.builder().firstLine("Marszałkowska 100").city("Warsaw")
            .zipCode("01-007").district("Śródmieście")
            .country("PL")
            .build())
        .description("Cool Cat to kultowa restauracja asian fusion na mapie Śródmieścia")
        .googleMapsReference("gm reference").placeType(restType).build();

    var owList = List.of(new OpeningWindow(null,
            DayOfWeek.MONDAY,
            LocalTime.of(10, 00),
            LocalTime.of(20, 30),
            null),
        new OpeningWindow(null,
            DayOfWeek.TUESDAY,
            LocalTime.of(10, 00),
            LocalTime.of(20, 30),
            null),
        new OpeningWindow(null,
            DayOfWeek.WEDNESDAY,
            LocalTime.of(10, 00),
            LocalTime.of(20, 30),
            null),
        new OpeningWindow(null,
            DayOfWeek.THURSDAY,
            LocalTime.of(10, 00),
            LocalTime.of(20, 30),
            null),
        new OpeningWindow(null,
            DayOfWeek.FRIDAY,
            LocalTime.of(10, 00),
            LocalTime.of(22, 30),
            null));

    var oe = List.of(new PlaceOpeningException(null, LocalDate.of(2024, 03, 12), List.of(), null));
    owList.forEach(r1::addStandardOpeningTime);
    oe.forEach(r1::addOpeningException);
    placeRepository.saveAndFlush(r1);

  }
}