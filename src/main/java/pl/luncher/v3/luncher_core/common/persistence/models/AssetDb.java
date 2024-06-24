package pl.luncher.v3.luncher_core.common.persistence.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToOne;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

//@MappedSuperclass
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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

  @OneToOne
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
