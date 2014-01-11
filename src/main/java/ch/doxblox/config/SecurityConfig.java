package ch.doxblox.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ch.doxblox.security.authentication.RestAuthenticationEntryPoint;
import ch.doxblox.security.authentication.RestAuthenticationFailureHandler;
import ch.doxblox.security.authentication.RestAuthenticationSuccessHandler;
import ch.doxblox.security.authentication.RestLogoutSuccessHandler;
import ch.doxblox.service.impl.MyUserDetailsService;

/**
 * Configuration for web based security.
 * 
 * @author Marco Jakob
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  
  private MyUserDetailsService myUserDetailsService;

  @Inject
  public SecurityConfig(MyUserDetailsService myUserDetailsService) {
    this.myUserDetailsService = myUserDetailsService;
  }
  
  @Override
  public void configure(WebSecurity web) throws Exception {
    // Do not secure following paths.
    web.ignoring().antMatchers(
        "/index.html",
        "/doxblox.dart.js",
        "/doxblox.css",
        "/packages/**",
        "/resources/**");
  }
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // Add filters to the Spring security filter chain. 
    // The order of the filters is: logout, login, basic auth.

    // The logout filter.
    http.logout()
      .logoutSuccessHandler(new RestLogoutSuccessHandler());
    
    // The login filter.
    UsernamePasswordAuthenticationFilter loginFilter = new UsernamePasswordAuthenticationFilter();
    loginFilter.setFilterProcessesUrl("/login");
    loginFilter.setAuthenticationManager(http.getAuthenticationManager());
    // custom authentication success handler that returns the HTTP status code 200 instead of 301. 
    loginFilter.setAuthenticationSuccessHandler(new RestAuthenticationSuccessHandler());
    // custom authentication failure handler
    loginFilter.setAuthenticationFailureHandler(new RestAuthenticationFailureHandler());
    loginFilter.setUsernameParameter("username");
    loginFilter.setPasswordParameter("password");
    http.addFilter(loginFilter);
    
    // The basic auth filter.
    http.httpBasic()
      // Entry point just returns 401-unauthorized for every unauthorized request. Client must handle
      // this and direct user to login page.
      .authenticationEntryPoint(new RestAuthenticationEntryPoint());
  }
  
  @Override
  protected void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    // Configure UserDetailsService implementation.
    auth
      .userDetailsService(myUserDetailsService)
        .passwordEncoder(new BCryptPasswordEncoder());
  }
}