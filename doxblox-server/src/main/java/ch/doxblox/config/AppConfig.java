package ch.doxblox.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration of application beans.
 * 
 * @author Marco Jakob
 */
@Configuration
@ComponentScan("ch.doxblox.service.impl") // Contains all @Service classes.
class AppConfig {
}