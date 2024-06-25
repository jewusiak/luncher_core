package pl.luncher.v3.luncher_core.common.utils;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.admin.model.requests.AdminPlaceCreationRequest;
import pl.luncher.v3.luncher_core.common.assets.Asset;
import pl.luncher.v3.luncher_core.common.assets.AssetFactory;
import pl.luncher.v3.luncher_core.common.domain.infra.AppRole;
import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.place.PlaceFactory;
import pl.luncher.v3.luncher_core.common.place.valueobject.Address;
import pl.luncher.v3.luncher_core.common.place.valueobject.OpeningWindowDto;
import pl.luncher.v3.luncher_core.common.placetype.PlaceType;
import pl.luncher.v3.luncher_core.common.placetype.PlaceTypeFactory;
import pl.luncher.v3.luncher_core.common.repositories.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultDataGenerator {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final PlaceTypeFactory placeTypeFactory;
  private final PlaceFactory placeFactory;
  private final AssetFactory assetFactory;


  @EventListener(ApplicationReadyEvent.class)
  public void seedData() {

    //USERS
    List<User> users = List.of(
        User.builder().firstName("Grzegorz").surname("Root").email("root@g.pl")
            .role(AppRole.SYS_ROOT).passwordHash(passwordEncoder.encode("1234")).enabled(true)
            .build(), User.builder().firstName("Grzegorz").surname("Admin").email("admin@g.pl")
            .role(AppRole.SYS_ADMIN).passwordHash(passwordEncoder.encode("1234")).enabled(true)
            .build(),
        User.builder().firstName("Grzegorz").surname("Mod").email("mod@g.pl").role(AppRole.SYS_MOD)
            .passwordHash(passwordEncoder.encode("1234")).enabled(true).build(),
        User.builder().firstName("Grzegorz").surname("Manager").email("manager@g.pl")
            .role(AppRole.REST_MANAGER).enabled(true).passwordHash(passwordEncoder.encode("1234"))
            .build(),
        User.builder().firstName("Grzegorz").surname("Rest-User").email("employee@g.pl")
            .role(AppRole.REST_USER).passwordHash(passwordEncoder.encode("1234")).enabled(true)
            .build(), User.builder().firstName("Grzegorz").surname("End-User").email("user@g.pl")
            .role(AppRole.USER).passwordHash(passwordEncoder.encode("1234")).enabled(true).build());

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
    var restType = placeTypeFactory.of("RESTAURANT", "restaurant", "Restauracja");
    var barType = placeTypeFactory.of("BAR", "wine_bar", "Bar");
    var clubType = placeTypeFactory.of("CLUB", "nightlife", "Klub");

    List.of(restType, barType, clubType).forEach(PlaceType::save);

    //restaurants
    AdminPlaceCreationRequest r1 = AdminPlaceCreationRequest.builder().name("The Cool Cat")
        .longName("The Cool Cat - TR")
//        .allowedUsers(Set.of(employee))
        .address(Address.builder().firstLine("Marszałkowska 100").city("Warsaw").zipCode("01-007")
            .district("Śródmieście").country("PL").build())

        .description("Cool Cat to kultowa restauracja asian fusion na mapie Śródmieścia")
        .googleMapsReference("gm reference")
        //todo: implement resolving: .placeTypeIdentifier(restType.castToDto().getIdentifier())
        .build();

    var place = placeFactory.of(r1);

    var owList = List.of(
        new OpeningWindowDto(null, DayOfWeek.MONDAY, LocalTime.of(10, 00), LocalTime.of(20, 30),
            null),
        new OpeningWindowDto(null, DayOfWeek.TUESDAY, LocalTime.of(10, 00), LocalTime.of(20, 30),
            null),
        new OpeningWindowDto(null, DayOfWeek.WEDNESDAY, LocalTime.of(10, 00), LocalTime.of(20, 30),
            null),
        new OpeningWindowDto(null, DayOfWeek.THURSDAY, LocalTime.of(10, 00), LocalTime.of(20, 30),
            null),
        new OpeningWindowDto(null, DayOfWeek.FRIDAY, LocalTime.of(10, 00), LocalTime.of(22, 30),
            null));

    owList.forEach(place::addOpeningWindow);
    place.save();

    Asset asset = assetFactory.createImageAsset("exampleName", "exDescr", "jpg");
    System.out.println(asset.getUploadUrl());
    System.out.println("---------------------");
    System.out.println(asset.getAccessUrl());
  }
}
