package ch.documakery.web;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.server.samples.context.SecurityRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
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
import ch.documakery.domain.user.User;
import ch.documakery.domain.user.dto.UserRegisterDto;
import ch.documakery.repository.MongoDbTestUtils;
import ch.documakery.web.UserController;

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
  MongoTemplate template;
  
  private MockMvc mockMvc;
  
  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(springSecurityFilterChain)
            .alwaysDo(print())
            .build();
    
    MongoDbTestUtils.cleanDb(template);
    MongoDbTestUtils.addCorrectUserToDb(template);
  }
  
  @Test
  public void getUser_AsUser_ReturnUserAsJson() throws Exception {
    // when
    mockMvc.perform(get("/user")
        .with(userDetailsService(MongoDbTestUtils.CORRECT_USERNAME))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
    )
    // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(content().string(containsString("{\"email\":\"" + MongoDbTestUtils.CORRECT_USERNAME)));
    
  }

  @Test
  public void getUser_AsAnonymous_ReturnUnauthorized() throws Exception {
    // when
    mockMvc.perform(get("/user")
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
    )
    // then
        .andExpect(status().isUnauthorized());
  }
  
  @Test
  public void registerUser() throws Exception {
    // given
    UserRegisterDto userRegister = new UserRegisterDto();
    userRegister.setEmail("email@email.com");
    userRegister.setNickname("nick");
    userRegister.setPassword("password");
    
    // when
    mockMvc.perform(post("/user")
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        .content(JsonTestUtils.convertToJsonBytes(userRegister))
    )
    // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(content().string("{\"email\":\"email@email.com\",\"nickname\":\"nick\"}"));
  }
  
  @Test
  public void registerUser_InvalidEmail_ReturnBadRequest() throws Exception {
    // given
    UserRegisterDto userRegister = new UserRegisterDto();
    userRegister.setEmail("invalidemail.com");
    userRegister.setNickname("nick");
    userRegister.setPassword("password");
    
    // when
    mockMvc.perform(post("/user")
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        .content(JsonTestUtils.convertToJsonBytes(userRegister))
    )
    // then
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(content().string("{\"errors\":[{\"path\":\"email\",\"message\":\"not a well-formed email address\"}]}"));
  }
  
  @Test
  public void registerUser_EmailAndNicknameNotUnique_ReturnBadRequest() throws Exception {
    // given
    UserRegisterDto userRegister = new UserRegisterDto();
    userRegister.setEmail(MongoDbTestUtils.CORRECT_USERNAME);
    userRegister.setNickname(MongoDbTestUtils.CORRECT_NICKNAME);
    userRegister.setPassword("password");
    
    // when
    mockMvc.perform(post("/user")
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        .content(JsonTestUtils.convertToJsonBytes(userRegister))
        )
        // then
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(content().string(allOf(
            containsString("{\"path\":\"email\",\"message\":\"Email not unique\"}"),
            containsString("{\"path\":\"nickname\",\"message\":\"Nickname not unique\"}"))));
  }
  
  @Test
  public void deleteUser() throws Exception {
    // when
    mockMvc.perform(delete("/user")
        .with(userDetailsService(MongoDbTestUtils.CORRECT_USERNAME))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
    )
    // then
        .andExpect(status().isOk());
    assertThat(template.findAll(User.class).size(), is(0));
  }
  
  @Test
  public void deleteUser_AsAnonymous_ReturnUnauthorized() throws Exception {
    // when
    mockMvc.perform(delete("/user")
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
    )
    // then
        .andExpect(status().isUnauthorized());
    assertThat(template.findAll(User.class).size(), is(1));
  }
}
