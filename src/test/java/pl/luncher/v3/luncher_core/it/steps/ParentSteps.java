package pl.luncher.v3.luncher_core.it.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import pl.luncher.v3.luncher_core.common.model.responses.SuccessfulLoginResponse;

@Slf4j
@RequiredArgsConstructor
public class ParentSteps {

  private static ObjectMapper objectMapper = new ObjectMapper();

  private static Map<String, Object> cache = new HashMap<>();

  private static Response cachedResp;
  private static String currentJwtToken;

  public static <T> T getFromCache(String key, Class<T> tClass) {
    return tClass.cast(cache.get(key));
  }

  public static String getLastCreatedPlaceUuid() {
    return getFromCache("newPlaceUuid", String.class);
  }

  public static <T> void putToCache(String key, T obj) {
    if (cache.containsKey(key)) {
      log.warn("(overwriting) cache has had already cached a ({},{})", key, cache.get(key));
    }
    cache.put(key, obj);
  }

  public static void saveHttpResp(@NotNull Response response) {
    cachedResp = response;
  }

  public static void saveLoginResp(@NotNull Response response) {
    saveHttpResp(response);
    if (response.getStatusCode() == 200) {
      log.debug("Logged in successfully");
      currentJwtToken = response.getBody().as(SuccessfulLoginResponse.class).getAccessToken();
    } else {
      log.warn("Login with code {}", response.getStatusCode());
    }
  }

  public static void resetAll() {
    cache = new HashMap<>();
    cachedResp = null;
    currentJwtToken = null;
  }

  public static Response getCachedHttpResp() {
    return cachedResp;
  }

  public static <T> T castAny(Object obj, Class<T> tClass) {
    return obj == null ? null : tClass.cast(obj);
  }

  public static String getAuthorizationHeaderContent() {
    return currentJwtToken == null ? null : "Bearer %s".formatted(currentJwtToken);
  }

  public static <T> T castMap(Map<String, ?> map, Class<T> tClass) {
    return map == null ? null : objectMapper.convertValue(map, tClass);
  }

  public static RequestSpecification getRASpecificationWithAuthAndAcceptHeaders() {
    return RestAssured.given()
        .header("Authorization", getAuthorizationHeaderContent())
        .header("Content-Type", "application/json")
        .header("Accept", "application/json");
  }
}
