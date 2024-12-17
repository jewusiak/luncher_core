package pl.luncher.v3.luncher_core.configuration.searchengine;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.place.domainservices.PlaceSearchService;
import pl.luncher.v3.luncher_core.user.domainservices.interfaces.UserSearchService;

@Slf4j
@Component
@RequiredArgsConstructor
public class OnLoadRefreshIndexes {

  private final UserSearchService userSearchService;
  private final PlaceSearchService placeSearchService;

  @EventListener(ApplicationReadyEvent.class)
  @Transactional
  public void refreshIndexesOnStart() throws InterruptedException {
    log.info("Refreshing indexes begin...");
    userSearchService.reindexDb();
    placeSearchService.reindexDb();
    log.info("Refreshing indexes finished!");
  }
}
