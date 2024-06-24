package pl.luncher.v3.luncher_core.common.persistence.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "image_assets", schema = "luncher_core")
@DiscriminatorValue("IMAGE")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ImageAssetDb extends AssetDb {

  @ManyToOne
  private PlaceDb refToPlaceImages;

}
