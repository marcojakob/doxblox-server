package ch.documakery.web;

import static org.hamcrest.Matchers.*;
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
import ch.documakery.MongoDbTestUtils;
import ch.documakery.domain.document.QuestionBlock;
import ch.documakery.repository.QuestionBlockRepository;

/**
 * Integration test for {@link QuestionBlockController}.
 * 
 * @author Marco Jakob
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:META-INF/spring/applicationContext-*.xml"})
@ActiveProfiles("dev")
@WebAppConfiguration
public class QuestionBlockControllerIntegrationTest {
  
  @Inject
  private FilterChainProxy springSecurityFilterChain;

  @Inject
  private WebApplicationContext webApplicationContext;

  @Inject
  private MongoTemplate template;
  
  @Inject
  private QuestionBlockRepository questionBlockRepository;
  
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
  public void GETquestionblocks_AsUser_ReturnsAllQuestionBlocksOfUser() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    MongoDbTestUtils.importTestQuestionBlocks(template);
    
    // when
    mockMvc.perform(get("/questionblocks")
        .with(userDetailsService(MongoDbTestUtils.USER1_EMAIL))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
    )
    // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0].title", is("Inventar 1")))
        .andExpect(jsonPath("$[1].questions", hasSize(4)))
        .andExpect(jsonPath("$[2].title", is("Leitbild des Hotels/Restaurants Edelweiss")));
  }
  
  @Test
  public void GETquestionblocks_AsUserWithNoQuestionBlocks_ReturnsEmptyBody() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    // NOT importing question blocks
    
    // when
    mockMvc.perform(get("/questionblocks")
        .with(userDetailsService(MongoDbTestUtils.USER1_EMAIL))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
    )
    // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$").doesNotExist());
  }
  
  @Test
  public void GETQuestionblocks_AsAnonymous_ReturnsUnauthorized() throws Exception {
    // when
    mockMvc.perform(get("/questionblocks")
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
    )
    // then
        .andExpect(status().isUnauthorized());
  }
  
  @Test
  public void POSTquestionblocks_AsUserAndOneBlockAsParam_ReturnsOkAndQuestionBlockIsInDb() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    QuestionBlock questionBlock = new QuestionBlock();
    questionBlock.setTitle("Title of my new question block");
    
    // when
    mockMvc.perform(post("/questionblocks")
        .with(userDetailsService(MongoDbTestUtils.USER1_EMAIL))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        .content(JsonTestUtils.convertToJsonBytes(questionBlock))
    )
    // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.title", is("Title of my new question block")));
    
    assertThat(questionBlockRepository.count(), is(1L));
  }
  
  @Test
  public void POSTquestionblocks_AsAnonymous_ReturnsUnauthorizedAndQuestionBlockIsNotInDb() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    QuestionBlock questionBlock = new QuestionBlock();
    questionBlock.setTitle("Title of my new question block");
    
    // when
    mockMvc.perform(post("/questionblocks")
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        .content(JsonTestUtils.convertToJsonBytes(questionBlock))
    )
    // then
        .andExpect(status().isUnauthorized());
    
    assertThat(questionBlockRepository.count(), is(0L));
  }
  
  @Test
  public void POSTquestionblocks_InvalidTitle_ReturnsErrorAndQuestionBlockIsNotInDb() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    QuestionBlock questionBlock = new QuestionBlock();
    questionBlock.setTitle("");
    
    // when
    mockMvc.perform(post("/questionblocks")
        .with(userDetailsService(MongoDbTestUtils.USER1_EMAIL))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        .content(JsonTestUtils.convertToJsonBytes(questionBlock))
    )
    // then
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.errors[0].path", is("title")))
        .andExpect(jsonPath("$.errors[0].message", is("may not be empty")))
        .andExpect(jsonPath("$.errors", hasSize(1)));
    
    assertThat(questionBlockRepository.count(), is(0L));
  }
  
  @Test
  public void POSTquestionblocks_IllegalUserIdSet_QuestionBlockSavedButUserIdIsIgnored() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    // when
    // aaaaaa... is not the current user's id
    mockMvc.perform(post("/questionblocks")
        .with(userDetailsService(MongoDbTestUtils.USER1_EMAIL))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        .content("{\"id\":\"666666666666666666666666\",\"title\":\"just a title\",\"userId\":{\"$oid\":\"aaaaaaaaaaaaaaaaaaaaaaaa\"}}")
    )
    // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id", is("666666666666666666666666")))
        .andExpect(jsonPath("$.userId").doesNotExist())
        .andExpect(jsonPath("$.title", is("just a title")));
    
    QuestionBlock savedEntity = template.findById("666666666666666666666666", QuestionBlock.class);
    assertThat(savedEntity.getUserId(), is(MongoDbTestUtils.USER1_ID));
  }
}