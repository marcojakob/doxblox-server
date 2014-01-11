package ch.doxblox.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.WebApplicationInitializer;

/**
 * Registers the springSecurityFilterChain and maps it to "/*".
 * 
 * <p>
 * The {@link Order} ensures that it is initialized AFTER all other
 * {@link WebApplicationInitializer}s. This places the security filter on top of all other filters.
 * </p>
 * 
 * @author Marco Jakob
 */
@Order
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
}
