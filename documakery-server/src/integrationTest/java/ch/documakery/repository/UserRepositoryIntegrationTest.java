package ch.documakery.repository;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.documakery.MongoDbTestUtils;
import ch.documakery.domain.user.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

/**
 * Test class for {@link UserRepository}.
 * 
 * @author Marco Jakob
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:META-INF/spring/applicationContext-*.xml"})
@ActiveProfiles("dev")
@WebAppConfiguration
public class UserRepositoryIntegrationTest {
  
  static final String COLLECTION_NAME = "user";

  static final String EMAIL_1 = "user1@user.com";
  static final String NICKNAME_1 = "nickname1";
  static final String PASSWORD_1 = "someHashedPassword1";
  
  static final String EMAIL_2 = "user2@user.com";
  static final String NICKNAME_2 = "nickname2";
  static final String PASSWORD_2 = "someHashedPassword2";
  
  User user1;
  User user2;
  
  @Inject
  UserRepository repository; // SUT
  
  @Inject
  MongoTemplate template;

  @Before
  public void setUp() {
    MongoDbTestUtils.cleanDb(template);
    
    user1 = new User(EMAIL_1, PASSWORD_1);
    user1.setNickname(NICKNAME_1);
    
    user2 = new User(EMAIL_2, PASSWORD_2);
    user2.setNickname(NICKNAME_2);
  }
  
  @Test
  public void save() throws IOException {
    // when
    User user1Saved = repository.save(user1);
    User user2Saved = repository.save(user2);

    // then
    assertThat(user1Saved.getId(), is(notNullValue()));
    assertThat(user1Saved.getEmail(), is(EMAIL_1));
    assertThat(user1Saved.isEmailConfirmed(), is(false));
    assertThat(user1Saved.getPassword(), is(PASSWORD_1));
    assertThat(user1Saved.getAuthorities().size(), is(0));
    assertThat(user1Saved.getNickname(), is(NICKNAME_1));
    assertThat(user1Saved.isAccountNonExpired(), is(true));
    assertThat(user1Saved.isAccountNonLocked(), is(true));
    assertThat(user1Saved.isCredentialsNonExpired(), is(true));
    assertThat(user1Saved.isEnabled(), is(true));
    
    assertThat(user2Saved.getId(), is(notNullValue()));
    
    DBCollection collection = template.getCollection(COLLECTION_NAME);
    assertThat(collection.count(), is(2L));
    System.out.println(MongoDbTestUtils.convertToJsonString(collection.find(), true));
    
    BasicDBObject user1Db = (BasicDBObject) collection.findOne(new BasicDBObject("email", EMAIL_1));
    assertThat(user1Db.getString("email"), is(EMAIL_1));
    assertThat(user1Db.getString("password"), is(PASSWORD_1));
    assertThat(user1Db.getString("emailConfirmed"), is("false"));
    assertThat(user1Db.get("_id"), is(notNullValue()));
//    assertEquals(203, ((BasicDBObject) myDoc.get("info")).get("x"));
    
    BasicDBObject user2Db = (BasicDBObject) collection.findOne(new BasicDBObject("email", EMAIL_2));
    assertThat(user2Db.getString("email"), is(EMAIL_2));
    assertThat(user2Db.getString("password"), is(PASSWORD_2));
    assertThat(user2Db.getString("emailConfirmed"), is("false"));
    assertThat(user2Db.get("_id"), is(notNullValue()));
  }
  
  @Test
  public void save_UniqueEmailViolation_IgnoreSave() {
    // given
    template.save(user1);
    user2.setEmail(EMAIL_1);
    
    // when
    repository.save(user2);

    // then
    assertThat(template.findAll(User.class).size(), is(1));
  }
}
