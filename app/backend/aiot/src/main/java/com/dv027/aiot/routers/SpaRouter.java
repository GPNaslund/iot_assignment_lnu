package com.dv027.aiot.routers;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Class with the method for serving SPA application in the static folder.
 */
@Configuration(proxyBeanMethods = false)
public class SpaRouter {

  /**
   * Router method for serving the SPA in the static folder.
   * 
   * @return RouterFunction<ServerResponse>
   */
  @Bean("SpaRoute")
  public RouterFunction<ServerResponse> route() {
    ClassPathResource index = new ClassPathResource("static/index.html");
    List<String> extensions = Arrays.asList("js", "css", "ico", "png", "jpg", "jpeg", "gif", "TTF");

    RequestPredicate spaPredicate = RequestPredicates.path("/api/**")
        .or(RequestPredicates.path("/error"))
        .or(RequestPredicates.pathExtension(extensions::contains)).negate();

    RouterFunction<ServerResponse> spaRouter = RouterFunctions.route(spaPredicate,
        request -> ServerResponse.ok().contentType(MediaType.TEXT_HTML).bodyValue(index));

    RouterFunction<ServerResponse> staticFileRouter = RouterFunctions.resources("/**",
        new ClassPathResource("static/"));

    return staticFileRouter.and(spaRouter);
  }
}
