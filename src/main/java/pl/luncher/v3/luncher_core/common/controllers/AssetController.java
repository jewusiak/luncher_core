package pl.luncher.v3.luncher_core.common.controllers;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.luncher.v3.luncher_core.common.properties.GcpObjectStorageProperties;

@RestController("/asset")
@RequiredArgsConstructor
public class AssetController {

  //  private final Storage storage;
  private final GcpObjectStorageProperties gcpObjectStorageProperties;
  private final Storage storage;

  @PostMapping
  public ResponseEntity<?> create(@RequestParam String description) {
//    blobPersistenceObject.setUuid(UUID.randomUUID());
//
//    var storage = StorageOptions.newBuilder()
//        .setProjectId(gcpObjectStorageProperties.getProjectId()).setCredentials().build()
//        .getService();

    // Generate Signed URL
    Map<String, String> extensionHeaders = new HashMap<>();
    extensionHeaders.put("Content-Type", "image/png");

    Blob blobInfo = storage.get("", gcpObjectStorageProperties.getBucketName());
    URL url =
        storage.signUrl(
            blobInfo,
            15,
            TimeUnit.MINUTES,
            Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
            Storage.SignUrlOption.withExtHeaders(extensionHeaders),
            Storage.SignUrlOption.withV4Signature());

//    imageAssetDb.setBlobId(blob.getGeneratedId());

//    save();
    return ResponseEntity.ok(new Resp(blobInfo.getBlobId().getName(),
        url.toString()));
  }

  @Data
  @AllArgsConstructor
  public class Resp {

    private String uuid;
    private String url;
  }
}
