package pl.luncher.v3.luncher_core.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.luncher.v3.luncher_core.configuration.security.PermitAll;
import pl.luncher.v3.luncher_core.contentmanagement.domainservices.ContentArrangementService;
import pl.luncher.v3.luncher_core.controllers.dtos.contentmanagement.dtos.PageArrangementDto;
import pl.luncher.v3.luncher_core.controllers.dtos.contentmanagement.mappers.PageArrangementDtoMapper;
import pl.luncher.v3.luncher_core.user.model.AppRole.hasRole;

@Tag(name = "CMS")
@RestController
@RequestMapping("/content-management")
@RequiredArgsConstructor
public class AppContentController {

  private final ContentArrangementService contentArrangementService;
  private final PageArrangementDtoMapper pageArrangementDtoMapper;

  @GetMapping("/arrangements/primary")
  @PermitAll
  public ResponseEntity<PageArrangementDto> getPrimaryArrangementContents() {
    return ResponseEntity.ok(
        pageArrangementDtoMapper.toDto(contentArrangementService.getPrimaryArrangement()));
  }

  @GetMapping("/arrangements")
  @PreAuthorize(hasRole.SYS_ADMIN)
  public ResponseEntity<List<PageArrangementDto>> getAllArrangements() {
    return ResponseEntity.ok(contentArrangementService.getAllPageArrangements().stream()
        .map(pageArrangementDtoMapper::toDto).collect(Collectors.toList()));
  }

  @GetMapping("/arrangements/{uuid}")
  @PreAuthorize(hasRole.SYS_ADMIN)
  public ResponseEntity<PageArrangementDto> getArrangementById(@PathVariable UUID uuid) {
    return ResponseEntity.ok(
        pageArrangementDtoMapper.toDto(contentArrangementService.getArrangementById(uuid)));
  }

  @PostMapping("/arrangements")
  @PreAuthorize(hasRole.SYS_ADMIN)
  public ResponseEntity<PageArrangementDto> createArrangement(
      @RequestBody @Valid PageArrangementDto request) {
    return ResponseEntity.ok(pageArrangementDtoMapper.toDto(
        contentArrangementService.createNewArrangement(
            pageArrangementDtoMapper.toDomain(request))));
  }

  @PutMapping("/arrangements/{uuid}")
  @PreAuthorize(hasRole.SYS_ADMIN)
  public ResponseEntity<PageArrangementDto> updateArrangement(
      @RequestBody @Valid PageArrangementDto request, @PathVariable UUID uuid) {

    request.setId(uuid);

    return ResponseEntity.ok(pageArrangementDtoMapper.toDto(
        contentArrangementService.updateArrangement(pageArrangementDtoMapper.toDomain(request))));
  }

  @DeleteMapping("/arrangements/{uuid}")
  @PreAuthorize(hasRole.SYS_ADMIN)
  public ResponseEntity<?> deleteArrangement(@PathVariable UUID uuid) {
    contentArrangementService.deleteArrangementById(uuid);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/arrangements/{uuid}/primary")
  @PreAuthorize(hasRole.SYS_ADMIN)
  public ResponseEntity<PageArrangementDto> makeArrangementPrimary(@PathVariable UUID uuid) {
    return ResponseEntity.ok(
        pageArrangementDtoMapper.toDto(contentArrangementService.makeArrangementPrimary(uuid)));
  }
}
