package com.dv027.aiot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.http.HttpMethod;

import reactor.core.publisher.Mono;

/**
 * Class with method for filtering requests and validate the
 * presence of api key.
 *
 */
@Component
public class ApiKeyFilter implements WebFilter {
  private String apiKey;

  /**
   * Creates a new instance of ApiKeyFilter.
   *
   * @param env - Environment class to get environment variables from.
   */
  @Autowired
  public ApiKeyFilter(Environment env) {
    this.apiKey = env.getRequiredProperty("api-key");
  }

  /**
   * Validates specified routes for inclusion of api key.
   *
   * @param exchange - Object used to extract request, response etc.
   * @param chain    - Chain of filters and handlers, used to pass on the request.
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    ServerHttpRequest req = exchange.getRequest();
    HttpMethod method = req.getMethod();
    if (HttpMethod.POST.equals(method)) {
      HttpHeaders headers = req.getHeaders();
      if (validateApiKey(headers)) {
        return chain.filter(exchange);
      } else {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
      }
    } else {
      return chain.filter(exchange);
    }
  }

  /**
   * Helper method for validating api key from http headers.
   *
   * @param headers - Object containing provided headers.
   * @return - Boolean indicating if api key is provided in headers.
   */
  private boolean validateApiKey(HttpHeaders headers) {
    String providedApiKey = headers.getFirst("X-API-Key");
    return providedApiKey != null && providedApiKey.equals(this.apiKey);
  }
}
