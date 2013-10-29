package ch.doxblox.config;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import ch.doxblox.web.json.MyObjectMapper;

/**
 * Web layer configuration enabling Spring MVC, JSR-303 validation, message conversion and field
 * formatting.
 * 
 * @author Marco Jakob
 */
@Configuration
@EnableWebMvc
@ComponentScan("ch.doxblox.web") // Contains all @Controller classes.
public class WebConfig extends DelegatingWebMvcConfiguration {

  @Override
  protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    // Message converter with an ObjectMapper used for serializing MongoDb's ObjectId.
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(new MyObjectMapper());

    converters.add(converter);
  }

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer.defaultContentType(MediaType.APPLICATION_JSON);
  }

  @Override
  protected void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    // Ensures that dispatcher servlet can be mapped to '/' and that static resources are still
    // served by the containers default servlet.
    configurer.enable();
  }
}