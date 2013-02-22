package ch.documakery.web;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ch.documakery.JsonTestUtils;
import ch.documakery.domain.user.dto.UserRegisterDto;
import ch.documakery.repository.MongoDbTestUtils;
import ch.documakery.repository.UserRepository;

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
//            .alwaysDo(MockMvcResultHandlers.print()) 
            .build();
    
    MongoDbTestUtils.cleanDb(template);
  }
  
  @Test
  public void getUser_AsUser_ReturnUserAsJson() throws Exception {
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
  public void getUser_AsAnonymous_ReturnUnauthorized() throws Exception {
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
  public void registerUser_Valid_UserIsInDb() throws Exception {
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
  public void registerUser_InvalidEmail_ReturnErrorAndNotInDb() throws Exception {
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
  public void registerUser_EmailAndNicknameNotUnique_ReturnError() throws Exception {
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
  public void deleteUser_AsUser_ReturnOkAndUserDeletedInDb() throws Exception {
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
  public void deleteUser_AsAnonymous_ReturnUnauthorized() throws Exception {
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
