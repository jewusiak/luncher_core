package pl.luncher.v3.luncher_core.infrastructure.filesystem;

public interface FilesystemPersistentAssetsBasePathGetter {

  String getFilesystemPersistentAssetsBasePath();

  String getFilesystemPersistentAssetsBasePathWithTrailingSlash();
}
