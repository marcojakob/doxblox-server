package ch.doxblox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;


/**
 * Configuration of MongoDB.
 * 
 * @author Marco Jakob
 */
@Configuration
class TestDbConfig {
  
  @Bean(initMethod="init", destroyMethod="destroy")
  public MongoDbFactory mongoDbFactory() throws Exception {
    return new EmbeddedMongoDbFactory();
  }
}