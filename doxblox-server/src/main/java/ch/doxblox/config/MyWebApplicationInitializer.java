package ch.doxblox.config;

import javax.servlet.Filter;

import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Servlet 3.0 {@link WebApplicationInitializer} using Spring 3.2 convenient base class
 * {@link AbstractAnnotationConfigDispatcherServletInitializer}. It essentially sets up a root
 * application context from {@link AppConfig}, and a dispatcher servlet application context
 * from {@link WebConfig} for general Spring MVC customizations.
 * 
 * @author Marco Jakob
 */
@Order(1) // Initialize BEFORE SecurityWebApplicationInitializer.
public class MyWebApplicationInitializer extends
    AbstractAnnotationConfigDispatcherServletInitializer {

  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class<?>[] {
        AppConfig.class, 
        DbConfig.class, 
        SecurityConfig.class};
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class<?>[] {
        WebConfig.class};
  }

  @Override
  protected String[] getServletMappings() {
    return new String[] {"/"};
  }

  @Override
  protected Filter[] getServletFilters() {
    return super.getServletFilters();
  }
}
