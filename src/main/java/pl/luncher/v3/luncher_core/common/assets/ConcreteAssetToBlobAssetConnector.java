package pl.luncher.v3.luncher_core.common.assets;

/**
 * Defines function which allows to connect Concrete Asset to a Blob Asset
 */
interface ConcreteAssetToBlobAssetConnector {

  void connectToBlob(Object o, Class clazz);
}
