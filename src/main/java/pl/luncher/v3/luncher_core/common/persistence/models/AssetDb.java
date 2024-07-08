package pl.luncher.v3.luncher_core.common.persistence.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
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

  @OneToOne(cascade = CascadeType.ALL)
  private GcpAssetDb gcpAsset;

  public void setBlobAsset(Object blobAsset, Class clazz) {
    if (clazz == GcpAssetDb.class) {
      setGcpAsset((GcpAssetDb) blobAsset);
    } else {
      throw new IllegalArgumentException(
          "AssetDb doesnt support %s class".formatted(clazz.getSimpleName()));
    }
  }

}
