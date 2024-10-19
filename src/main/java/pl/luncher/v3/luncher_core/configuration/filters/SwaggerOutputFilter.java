package pl.luncher.v3.luncher_core.configuration.filters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class SwaggerOutputFilter implements Filter {

  private final ObjectMapper objectMapper;

  private static final List<String> SUPPORTED_PREFIXES = List.of("GET_", "POST_", "PUT_", "PATCH_", "DELETE_");

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    var urlPaths = ((HttpServletRequest) request).getServletPath().split("/");
    var lastPathPart = urlPaths[urlPaths.length - 1];


    if (SUPPORTED_PREFIXES.stream().noneMatch(method -> lastPathPart.indexOf(method) == 0)) {
      chain.doFilter(request, response);
      return;
    }


    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);


    chain.doFilter(request, responseWrapper);
    String originalDataAsString = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

    try {
      var map = objectMapper.readValue(originalDataAsString, new TypeReference<HashMap<String, Object>>() {
      });
      var newTitle = "%s (%s)".formatted(((Map<String, Object>) map.get("info")).get("title"), lastPathPart);

      ((Map<String, Object>) map.get("info")).put("title", newTitle);

      var newData = objectMapper.writeValueAsString(map);

      response.setContentLength(newData.length());
      response.getWriter().write(newData);
      response.getWriter().flush();
    } catch (RuntimeException e) {
      log.error("Could not process Swagger response body", e);
      response.setContentLength(originalDataAsString.length());
      response.getWriter().write(originalDataAsString);
      response.getWriter().flush();
    }

  }
}
