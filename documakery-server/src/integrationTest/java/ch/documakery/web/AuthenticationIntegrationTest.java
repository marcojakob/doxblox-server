package ch.documakery.web;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ch.documakery.domain.user.User;
import ch.documakery.repository.MongoDbTestUtils;

/**
 * Integration test for authentication (login/logout).
 * 
 * @author Marco Jakob
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:META-INF/spring/applicationContext-*.xml"})
@ActiveProfiles("dev")
@WebAppConfiguration
public class AuthenticationIntegrationTest {
  
  private static final String REQUEST_PARAMETER_USERNAME = "username";
  private static final String REQUEST_PARAMETER_PASSWORD = "password";
  
  @Inject
  private FilterChainProxy springSecurityFilterChain;

  @Inject
  private WebApplicationContext webApplicationContext;

  @Inject
  MongoTemplate template;
  
  private MockMvc mockMvc;
  private User correctUser;
  
  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(springSecurityFilterChain)
//            .alwaysDo(print()) 
            .build();
    
    MongoDbTestUtils.cleanDb(template);
    correctUser = MongoDbTestUtils.addCorrectUserToDb(template);
  }
  
  @Test
  public void login_CorrectCredentials_ReturnOk() throws Exception {
    // when
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(REQUEST_PARAMETER_USERNAME, MongoDbTestUtils.CORRECT_USERNAME)
        .param(REQUEST_PARAMETER_PASSWORD, MongoDbTestUtils.CORRECT_PASSWORD)
    )
    // then
        .andExpect(status().isOk());
  }
  
  @Test
  public void login_IncorrectCredentials_ReturnUnauthorized() throws Exception {
    // when
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(REQUEST_PARAMETER_USERNAME, MongoDbTestUtils.INCORRECT_EMAIL)
        .param(REQUEST_PARAMETER_PASSWORD, MongoDbTestUtils.INCORRECT_PASSWORD)
    )
    // then
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void login_IncorrectRequestMethod_ReturnUnauthorized() throws Exception {
    // when
    mockMvc.perform(get("/login")
        .param(REQUEST_PARAMETER_USERNAME, MongoDbTestUtils.CORRECT_USERNAME)
        .param(REQUEST_PARAMETER_PASSWORD, MongoDbTestUtils.CORRECT_PASSWORD)
    )
    // then
        .andExpect(status().isUnauthorized());
  }
  
  @Test
  public void login_EmailNotConfirmed_ReturnForbidden() throws Exception {
    // given
    correctUser.setEmailConfirmed(false);
    template.save(correctUser);
    
    // when
    MvcResult result = mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(REQUEST_PARAMETER_USERNAME, MongoDbTestUtils.CORRECT_USERNAME)
        .param(REQUEST_PARAMETER_PASSWORD, MongoDbTestUtils.CORRECT_PASSWORD)
    )
    // then
        .andExpect(status().isForbidden())
        .andReturn();
    assertThat(result.getResponse().getErrorMessage(), is("Email not confirmed"));
  }
  
  @Test
  public void logout_ReturnOk() throws Exception {
    // when
    mockMvc.perform(get("/logout")
        // .with(SecurityRequestPostProcessors.userDetailsService(CORRECT_USERNAME))
    )
    // then 
        .andExpect(status().isOk());
  }
}
