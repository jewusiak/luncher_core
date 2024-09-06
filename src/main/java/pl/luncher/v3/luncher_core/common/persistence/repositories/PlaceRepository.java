package pl.luncher.v3.luncher_core.common.persistence.repositories;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb;

public interface PlaceRepository extends JpaRepository<PlaceDb, UUID> {


  // dayOfWeek - stored as ordinal() - so 0 - Monday, but ISO DOW is 1 - Monday????
  @Query(value = "select p.* from luncher_core.places p left outer join luncher_core.opening_windows ow on p.id = ow.place_id where (:textQuery is null or p.name % :textQuery or p.description % :textQuery or p.longName % :textQuery) and (:placeTypeIdentifier is null or p.placetype_identifier = :placeTypeIdentifier) and (cast(:openAt as time) is null or cast(:dayOfWeek as smallint) is null or ow.dayofweek = :dayOfWeek and :openAt >= ow.starttime and :openAt < ow.endtime) and (cast(:location as geography) is null or cast(:radius as double precision) is null or st_dwithin(:location, p.location, :radius)) limit :maxSize offset :itemOffset", nativeQuery = true)
  List<PlaceDb> searchQuery(String textQuery, String placeTypeIdentifier, DayOfWeek dayOfWeek,
      LocalTime openAt, Point location, Double radius, int maxSize, int itemOffset);
}