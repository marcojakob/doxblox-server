package ch.doxblox.web;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.server.samples.context.SecurityRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ch.doxblox.JsonTestUtils;
import ch.doxblox.MongoDbTestUtils;
import ch.doxblox.domain.user.dto.UserRegisterDto;
import ch.doxblox.repository.UserRepository;
import ch.doxblox.web.UserController;

/**
 * Integration test for {@link UserController}.
 * 
 * @author Marco Jakob
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:META-INF/spring/applicationContext-*.xml"})
@ActiveProfiles("dev")
@WebAppConfiguration
public class UserControllerIntegrationTest {
  
  @Inject
  private FilterChainProxy springSecurityFilterChain;

  @Inject
  private WebApplicationContext webApplicationContext;

  @Inject
  private MongoTemplate template;
  
  @Inject
  private UserRepository userRepository;
  
  private MockMvc mockMvc;
  
  @Before
  public void setUp() throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(springSecurityFilterChain)
            .alwaysDo(MockMvcResultHandlers.print()) 
            .build();
    
    MongoDbTestUtils.cleanDb(template);
  }
  
  @Test
  public void GETusers_BasicAuthHeaderAndCorrectCredentials_ReturnsOk() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    // when
    mockMvc.perform(get("/users")
        .header("Authorization", MongoDbTestUtils.createBasicAuthHeader(
            MongoDbTestUtils.USER1_EMAIL, MongoDbTestUtils.USER1_PASSWORD))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
    )
    // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.email", is(MongoDbTestUtils.USER1_EMAIL)));
  }
  
  @Test
  public void GETusers_BasicAuthHeaderAndIncorrectCredentials_ReturnsUnauthorized() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    // when
    mockMvc.perform(get("/users")
        .header("Authorization", MongoDbTestUtils.createBasicAuthHeader(
            MongoDbTestUtils.USER1_EMAIL, "wrongPassword"))
            .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        )
        // then
        .andExpect(status().isUnauthorized());
  }



  @Test
  public void GETusers_AsUser_ReturnsUserAsJson() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    // when
    mockMvc.perform(get("/users")
        .with(userDetailsService(MongoDbTestUtils.USER1_EMAIL))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
    )
    // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.email", is(MongoDbTestUtils.USER1_EMAIL)));
  }

  @Test
  public void GETusers_AsAnonymous_ReturnsUnauthorized() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    // when
    mockMvc.perform(get("/users")
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
    )
    // then
        .andExpect(status().isUnauthorized());
  }
  
  @Test
  public void POSTusers_Valid_UserIsInDb() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    UserRegisterDto userRegister = new UserRegisterDto();
    userRegister.setEmail("email@email.com");
    userRegister.setNickname("nick");
    userRegister.setPassword("password");
    
    // when
    mockMvc.perform(post("/users")
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        .content(JsonTestUtils.convertToJsonBytes(userRegister))
    )
    // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.email", is("email@email.com")))
        .andExpect(jsonPath("$.nickname", is("nick")));
    
    assertThat(userRepository.findByEmail("email@email.com"), is(notNullValue()));
  }
  
  @Test
  public void POSTusers_InvalidEmail_ReturnsErrorAndNotInDb() throws Exception {
    // given
    UserRegisterDto userRegister = new UserRegisterDto();
    userRegister.setEmail("invalidemail.com");
    userRegister.setNickname("nick");
    userRegister.setPassword("password");
    
    // when
    mockMvc.perform(post("/users")
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        .content(JsonTestUtils.convertToJsonBytes(userRegister))
    )
    // then
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.errors[0].path", is("email")))
        .andExpect(jsonPath("$.errors[0].message", is("not a well-formed email address")));
    
    assertThat(userRepository.count(), is(0L));
  }
  
  @Test
  public void POSTusers_EmailAndNicknameNotUnique_ReturnsError() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    UserRegisterDto userRegister = new UserRegisterDto();
    userRegister.setEmail(MongoDbTestUtils.USER1_EMAIL);
    userRegister.setNickname(MongoDbTestUtils.USER1_NICKNAME);
    userRegister.setPassword("password");
    
    // when
    mockMvc.perform(post("/users")
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        .content(JsonTestUtils.convertToJsonBytes(userRegister))
        )
        // then
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$..path", hasItems("email", "nickname")));
  }
  
  @Test
  public void DELETEusers_AsUser_ReturnsOkAndUserDeletedInDb() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    // when
    mockMvc.perform(delete("/users")
        .with(userDetailsService(MongoDbTestUtils.USER1_EMAIL))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
    )
    // then
        .andExpect(status().isOk());
    assertThat(userRepository.count(), is(1L));
  }
  
  @Test
  public void DELETEusers_AsAnonymous_ReturnsUnauthorized() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    // when
    mockMvc.perform(delete("/users")
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
    )
    // then
        .andExpect(status().isUnauthorized());
    assertThat(userRepository.count(), is(2L));
  }
}
