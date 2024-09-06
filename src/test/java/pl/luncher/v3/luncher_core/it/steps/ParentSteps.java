package pl.luncher.v3.luncher_core.it.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import pl.luncher.v3.luncher_core.common.controllers.errorhandling.model.ErrorResponse;
import pl.luncher.v3.luncher_core.common.model.responses.SuccessfulLoginResponse;

@Slf4j
@RequiredArgsConstructor
public class ParentSteps {

  private static ObjectMapper objectMapper = new ObjectMapper();

  private static Map<String, Object> cache = new HashMap<>();

  private static List<Map<Integer, String>> entityIds = initializeEntityIdsCache();

  private static Response cachedResp;
  private static String currentJwtToken;

  public static <T> T getFromCache(String key, Class<T> tClass) {
    return tClass.cast(cache.get(key));
  }

  public static <T> void putToCache(String key, T obj) {
    if (cache.containsKey(key)) {
      log.warn("(overwriting) cache has had already cached a ({},{})", key, cache.get(key));
    }
    cache.put(key, obj);
  }

  public static void saveHttpResp(@NotNull Response response) {
//    log.info("Saving HTTP response {}\n{}", response.getStatusCode(), response.getBody().prettyPrint());
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
    entityIds = initializeEntityIdsCache();
    cachedResp = null;
    currentJwtToken = null;
  }

  private static List<Map<Integer, String>> initializeEntityIdsCache() {
    List<Map<Integer, String>> list = new ArrayList<>();
    for (var ignored : EntityIdType.values()) {
      list.add(new HashMap<>());
    }
    return list;
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

  public static void removeAuthorizationToken() {
    currentJwtToken = null;
  }

  public static Object getResponseBody(Response resp, Class<?> tClass) {
    if (resp.getStatusCode() < 300 && resp.getStatusCode() >= 200) {
      return resp.as(tClass);
    }
    return resp.as(ErrorResponse.class);
  }
  
  public static Object castMapWithErrorHandling(Map<String, String> map, Class<?> tClass, int respStatusCode) {
    if (respStatusCode >= 200 && respStatusCode < 300) {
      return castMap(map, tClass);
    }
    return castMap(map, ErrorResponse.class);
  }  
  
  public static <T> T castMap(Map<String, String> map, Class<T> tClass) {
    if (map == null || map.containsKey(null)) {
      return null;
    }

    Map<String, Object> xMap = new HashMap<>();

    map.forEach((k, v) -> putIntoMap(xMap, k, v));

    return objectMapper.convertValue(xMap, tClass);
  }

  private static void putIntoMap(Map<String, Object> map, String k, String val) {
    String[] keys = k.split("\\.");
    if (keys.length > 1) {
      if (!map.containsKey(keys[0])) {
        map.put(keys[0], new HashMap<String, Object>());
      }

      String newKey = String.join(".", Arrays.stream(keys).toList().subList(1, keys.length));

      putIntoMap((Map<String, Object>) map.get(keys[0]), newKey, val);
    } else {
      map.put(keys[0], xNull(val, String.class));
    }

  }

  public static RequestSpecification givenHttpRequest() {
    RequestSpecification specification = RestAssured.given()
        .header("Content-Type", "application/json")
        .header("Accept", "application/json");
    String authorizationHeaderContent = getAuthorizationHeaderContent();
    if (authorizationHeaderContent != null) {
      specification.header("Authorization", authorizationHeaderContent);
    }
    return specification;
  }

  public static <T> T xNull(String value, Class<T> tClass) {
    if (value == null || value.isEmpty()) {
      return null;
    }
    if (value.equals("\"\"")) {
      return tClass.cast("");
    }

    return tClass.cast(value);
  }

  public static void putIdToCache(String value, int idx, EntityIdType entityIdType) {
    if (entityIds.get(entityIdType.getIndex()).containsKey(idx)) {
      log.info("Putting {} in place of {} (at index {}).", value, entityIds.get(entityIdType.getIndex()).get(idx), idx);
    }
    entityIds.get(entityIdType.getIndex()).put(idx, value);
    entityIds.get(entityIdType.getIndex()).put(-1, value);
    log.info("Putting {} at {} and -1", value, idx);
  }

  public static String getIdFromCache(String inputIdx, EntityIdType entityIdType) {
    int idx;
    if (inputIdx == null) {
      idx = -1;
    } else {
      try {
        UUID uuid = UUID.fromString(inputIdx);
        return uuid.toString();
      } catch (IllegalArgumentException ignored) {
      }
      idx = Integer.parseInt(inputIdx);
    }
    if (!entityIds.get(entityIdType.getIndex()).containsKey(idx)) {
      throw new RuntimeException("Id %d does not exist in cache".formatted(idx));
    }
    return entityIds.get(entityIdType.getIndex()).get(idx);
  }

  @RequiredArgsConstructor
  @Getter
  public enum EntityIdType {
    PLACE(0), ASSET(1), USER(2);

    private final int index;
  }
}
