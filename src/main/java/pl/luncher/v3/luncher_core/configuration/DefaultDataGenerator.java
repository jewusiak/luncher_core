package pl.luncher.v3.luncher_core.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.configuration.jwtUtils.JwtService;
import pl.luncher.v3.luncher_core.place.domainservices.PlacePersistenceService;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.placetype.domainservices.PlaceTypePersistenceService;
import pl.luncher.v3.luncher_core.placetype.model.PlaceType;
import pl.luncher.v3.luncher_core.user.domainservices.UserPersistenceService;
import pl.luncher.v3.luncher_core.user.model.AppRole;
import pl.luncher.v3.luncher_core.user.model.User;

@Slf4j
@RequiredArgsConstructor
@Component
@Profile({"local_dev", "local_test", "gcp_test"})
public class DefaultDataGenerator {

  private final PasswordEncoder passwordEncoder;
  private final UserPersistenceService userPersistenceService;
  private final PlaceTypePersistenceService placeTypePersistenceService;
  private final PlacePersistenceService placePersistenceService;
  private final JwtService jwtService;

  @EventListener(ApplicationReadyEvent.class)
  public void seedData() {
    var root = new User();
    root.setRole(AppRole.SYS_ROOT);
    root.setFirstName("Grzegorz");
    root.setSurname("Root");
    root.setEmail("root@luncher.pl");
    root.setPasswordHash(passwordEncoder.encode("1234"));
    root.setEnabled(true);
    try {
      userPersistenceService.save(root);
    } catch (RuntimeException exception) {
      log.info("Failed to save the user", exception);
    }

    var pt = new PlaceType("BAR", "BAR_ICON", "Bar");
    try {
      pt = placeTypePersistenceService.create(pt);
    } catch (Exception e) {
      log.error("", e);
    }
    PlaceType finalPt = pt;
    Place place = new Place() {{
      setName("Nowe miejsce");
      setEnabled(true);
      setPlaceType(finalPt);
    }};
    try {
      place = placePersistenceService.save(place);
      log.info("Created Place: {}", place);
    } catch (Exception e) {
      log.error("", e);
    }

    log.info("Token for {} user:\n{}", root.getEmail(), jwtService.generateJwtTokenForUser(userPersistenceService.getByEmail(root.getEmail())));


    //USERS
//    List<User> users = List.of(
//        User.builder().firstName("Grzegorz").surname("Root").email("root@g.pl")
//            .role(AppRole.SYS_ROOT).passwordHash(passwordEncoder.encode("1234")).enabled(true)
//            .build(), User.builder().firstName("Grzegorz").surname("Admin").email("admin@g.pl")
//            .role(AppRole.SYS_ADMIN).passwordHash(passwordEncoder.encode("1234")).enabled(true)
//            .build(),
//        User.builder().firstName("Grzegorz").surname("Mod").email("mod@g.pl").role(AppRole.SYS_MOD)
//            .passwordHash(passwordEncoder.encode("1234")).enabled(true).build(),
//        User.builder().firstName("Grzegorz").surname("Manager").email("manager@g.pl")
//            .role(AppRole.REST_MANAGER).enabled(true).passwordHash(passwordEncoder.encode("1234"))
//            .build(),
//        User.builder().firstName("Grzegorz").surname("Rest-User").email("employee@g.pl")
//            .role(AppRole.REST_MANAGER).passwordHash(passwordEncoder.encode("1234")).enabled(true)
//            .build(), User.builder().firstName("Grzegorz").surname("End-User").email("user@g.pl")
//            .role(AppRole.USER).passwordHash(passwordEncoder.encode("1234")).enabled(true).build());
//
//    for (User user : users) {
//      if (userRepository.existsByEmail(user.getEmail())) {
//        log.info("User {} already exists. Skipping.", user.getEmail());
//        continue;
//      }
//      userRepository.saveAndFlush(user);
//      log.info("Created user {} {} ({})", user.getFirstName(), user.getSurname(), user.getEmail());
//    }
//
//    User owner = userRepository.findUserByEmail("manager@g.pl").orElseThrow();
//    User employee = userRepository.findUserByEmail("employee@g.pl").orElseThrow();

//    //place types
//    var restType = placeTypeFactory.of("RESTAURANT", "restaurant", "Restauracja");
//    var barType = placeTypeFactory.of("BAR", "wine_bar", "Bar");
//    var clubType = placeTypeFactory.of("CLUB", "nightlife", "Klub");
//
//    List.of(restType, barType, clubType).forEach(PlaceType::save);
//
//    //restaurants
//    CreatePlaceRequest r1 = CreatePlaceRequest.builder().name("The Cool Cat")
//        .longName("The Cool Cat - TR")
////        .allowedUsers(Set.of(employee))
//        .address(Address.builder().firstLine("Marszałkowska 100").city("Warsaw").zipCode("01-007")
//            .district("Śródmieście").country("PL").build())
//
//        .description("Cool Cat to kultowa restauracja asian fusion na mapie Śródmieścia")
//        .googleMapsReference("gm reference")
//        .placeTypeIdentifier(restType.castToDto().getIdentifier())
//        .build();
//
//    var place = placeFactory.of(r1, owner);
//
//    var owList = List.of(
//        new OpeningWindowDto(null, DayOfWeek.MONDAY, LocalTime.of(10, 00), LocalTime.of(20, 30),
//            null),
//        new OpeningWindowDto(null, DayOfWeek.TUESDAY, LocalTime.of(10, 00), LocalTime.of(20, 30),
//            null),
//        new OpeningWindowDto(null, DayOfWeek.WEDNESDAY, LocalTime.of(10, 00), LocalTime.of(20, 30),
//            null),
//        new OpeningWindowDto(null, DayOfWeek.THURSDAY, LocalTime.of(10, 00), LocalTime.of(20, 30),
//            null),
//        new OpeningWindowDto(null, DayOfWeek.FRIDAY, LocalTime.of(10, 00), LocalTime.of(22, 30),
//            null));
//
//    owList.forEach(place::addOpeningWindow);
//    place.save();
//
//    Asset asset = assetFactory.createCommonAsset("exampleName", "exDescr", "jpg");
//    System.out.println(asset.getUploadUrl());
//    System.out.println("---------------------");
//    System.out.println(asset.getAccessUrl());
  }
}
