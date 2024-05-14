package pl.luncher.v3.luncher_core.common.persistence.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.net.URI;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "image_assets", schema = "luncher_core")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder(access = AccessLevel.MODULE)
@Getter
@Setter
public class ImageAssetDb {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;

  private URI uri;

  private String caption;

  private String filename;
}
