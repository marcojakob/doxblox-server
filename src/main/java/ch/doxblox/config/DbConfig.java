package ch.doxblox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.Mongo;

/**
 * Configuration of MongoDB.
 * 
 * @author Marco Jakob
 */
@Configuration
class DbConfig {
  
  /**
   * Note: CloudFoundry will automatically reconfigure the {@link MongoDbFactory} with its own
   * values for host, port, username, password and dbname.
   */
  @Bean
  public MongoDbFactory mongoDbFactory() throws Exception {
    return new SimpleMongoDbFactory(new Mongo("localhost", 27017), "doxblox");
  }
}