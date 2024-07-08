package pl.luncher.v3.luncher_core.common.persistence.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "assets", schema = "luncher_core")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class AssetDb {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;

  private String name;
  private String description;
  private String bucketPath;
  private String bucketName;
  private OffsetDateTime dateCreated;
  private String publicUrl;

  @ManyToOne
  private PlaceDb refToPlaceImages;


}
