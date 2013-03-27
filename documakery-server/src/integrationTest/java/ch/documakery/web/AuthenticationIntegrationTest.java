package ch.documakery.web;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.query.Query.*;
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

import ch.documakery.MongoDbTestUtils;
import ch.documakery.domain.user.User;

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
  
  @Before
  public void setUp() throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(springSecurityFilterChain)
//            .alwaysDo(MockMvcResultHandlers.print()) 
            .build();
    
    MongoDbTestUtils.cleanDb(template);
    MongoDbTestUtils.importTestUsers(template);
  }
  
  @Test
  public void POSTlogin_CorrectCredentials_ReturnsOk() throws Exception {
    // when
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(REQUEST_PARAMETER_USERNAME, MongoDbTestUtils.USER1_EMAIL)
        .param(REQUEST_PARAMETER_PASSWORD, MongoDbTestUtils.USER1_PASSWORD)
    )
    // then
        .andExpect(status().isOk());
  }
  
  @Test
  public void POSTlogin_IncorrectCredentials_ReturnsUnauthorized() throws Exception {
    // when
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(REQUEST_PARAMETER_USERNAME, "alskdfwle@fjasldea.com")
        .param(REQUEST_PARAMETER_PASSWORD, "asdfalkdflwüü")
    )
    // then
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void GETlogin_IncorrectRequestMethod_ReturnsUnauthorized() throws Exception {
    // when
    mockMvc.perform(get("/login")
        .param(REQUEST_PARAMETER_USERNAME, MongoDbTestUtils.USER1_EMAIL)
        .param(REQUEST_PARAMETER_PASSWORD, MongoDbTestUtils.USER1_PASSWORD)
    )
    // then
        .andExpect(status().isUnauthorized());
  }
  
  @Test
  public void POSTlogin_EmailNotConfirmed_ReturnsForbidden() throws Exception {
    // given
    User correctUser = template.findOne(query(where("email").is(MongoDbTestUtils.USER1_EMAIL)), User.class);
    correctUser.setEmailConfirmed(false);
    template.save(correctUser);
    
    // when
    MvcResult result = mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(REQUEST_PARAMETER_USERNAME, MongoDbTestUtils.USER1_EMAIL)
        .param(REQUEST_PARAMETER_PASSWORD, MongoDbTestUtils.USER1_PASSWORD)
    )
    // then
        .andExpect(status().isForbidden())
        .andReturn();
    assertThat(result.getResponse().getErrorMessage(), is("Email not confirmed"));
  }
  
  @Test
  public void GETlogout_ReturnOk() throws Exception {
    // when
    mockMvc.perform(get("/logout")
        // .with(SecurityRequestPostProcessors.userDetailsService(CORRECT_USERNAME))
    )
    // then 
        .andExpect(status().isOk());
  }
}
