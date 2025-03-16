package pl.luncher.v3.luncher_core.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import net.iakovlev.timeshape.TimeZoneEngine;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.luncher.v3.luncher_core.user.model.AppRole.hasRole;

@Tag(name = "Utilities")
@RestController
@RequestMapping("/utils")
@RequiredArgsConstructor
public class UtilsController {

  private final TimeZoneEngine timeZoneEngine;

  @Operation(summary = "Returns all timezones")
  @PreAuthorize(hasRole.REST_MANAGER)
  @GetMapping("/tz")
  public ResponseEntity<List<String>> getAvailableTimezones() {
    var list = new ArrayList<String>();
    list.add("Europe/Warsaw");
    list.addAll(ZoneId.getAvailableZoneIds().stream().filter(e -> !list.contains(e)).toList());
    return ResponseEntity.ok(list);
  }

  @Operation(summary = "Returns timezone by lat/lon")
  @PreAuthorize(hasRole.REST_MANAGER)
  @GetMapping("/tzquery")
  public ResponseEntity<String> getTimezone(@RequestParam Double lat, @RequestParam Double lon) {
    return ResponseEntity.ok(timeZoneEngine.query(lat, lon).orElseThrow(
            () -> new NoSuchElementException("Timezone at %g,%g not found!".formatted(lat, lon)))
        .getId());
  }
}
