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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ch.documakery.JsonTestUtils;
import ch.documakery.domain.document.Document;
import ch.documakery.repository.DocumentRepository;
import ch.documakery.repository.MongoDbTestUtils;

/**
 * Integration test for {@link DocumentController}.
 * 
 * @author Marco Jakob
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:META-INF/spring/applicationContext-*.xml"})
@ActiveProfiles("dev")
@WebAppConfiguration
public class DocumentControllerIntegrationTest {
  
  @Inject
  private FilterChainProxy springSecurityFilterChain;

  @Inject
  private WebApplicationContext webApplicationContext;

  @Inject
  private MongoTemplate template;
  
  @Inject
  private DocumentRepository documentRepository;
  
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
  public void getAllDocumentsOfUser_AsUser_ReturnDocuments() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    MongoDbTestUtils.importTestDocuments(template);
    
    // when
    mockMvc.perform(get("/document")
        .with(userDetailsService(MongoDbTestUtils.USER1_EMAIL))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
    )
    // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$..name", hasItems(
            "Prüfung - Inventar, Bilanz, Bilanzveränderung", 
            "Prüfung - Konto, Journal, Hauptbuch")));
  }
  
  
  @Test
  public void getAllDocumentsOfUser_AsUserNoDocuments_ReturnEmptyBody() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    // when
    mockMvc.perform(get("/document")
        .with(userDetailsService(MongoDbTestUtils.USER1_EMAIL))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
    )
    // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$").doesNotExist());
  }
  
  @Test
  public void getAllDocumentsOfUser_AsAnonymous_ReturnUnauthorized() throws Exception {
    // when
    mockMvc.perform(get("/document")
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
    )
    // then
        .andExpect(status().isUnauthorized());
  }
  
  @Test
  public void saveDocument_AsUser_ReturnOkAndDocumentInDb() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    Document document = new Document();
    document.setName("New Doc Name");
    
    // when
    mockMvc.perform(post("/document")
        .with(userDetailsService(MongoDbTestUtils.USER1_EMAIL))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        .content(JsonTestUtils.convertToJsonBytes(document))
    )
    // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.name", is("New Doc Name")));
    
    assertThat(documentRepository.count(), is(1L));
  }
  
  @Test
  public void saveDocument_AsAnonymous_ReturnUnauthorizedAndDocumentNotInDb() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    Document document = new Document();
    document.setName("New Doc Name");
    
    // when
    mockMvc.perform(post("/document")
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        .content(JsonTestUtils.convertToJsonBytes(document))
    )
    // then
        .andExpect(status().isUnauthorized());
    
    assertThat(documentRepository.count(), is(0L));
  }
  
  @Test
  public void saveDocument_InvalidName_ReturnErrorAndDocumentNotInDb() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    Document document = new Document();
    document.setName("");
    
    // when
    mockMvc.perform(post("/document")
        .with(userDetailsService(MongoDbTestUtils.USER1_EMAIL))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        .content(JsonTestUtils.convertToJsonBytes(document))
    )
    // then
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.errors[0].path", is("name")))
        .andExpect(jsonPath("$.errors[0].message", is("may not be empty")))
        .andExpect(jsonPath("$.errors[1]").doesNotExist());
    
    assertThat(documentRepository.count(), is(0L));
  }
  
  @Test
  public void saveDocument_IllegalUserIdSet_DocumentSavedButUserIdIsIgnored() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    // when
    mockMvc.perform(post("/document")
        .with(userDetailsService(MongoDbTestUtils.USER1_EMAIL))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        .content("{\"id\":\"666666666666666666666666\",\"name\":\"docName\",\"documentBlockIds\":[],\"userId\":{\"$oid\":\"aaaaaaaaaaaaaaaaaaaaaaaa\"}}")
    )
    // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id", is("666666666666666666666666")))
        .andExpect(jsonPath("$.userId").doesNotExist())
        .andExpect(jsonPath("$.name", is("docName")));
    
    Document savedEntity = template.findById("666666666666666666666666", Document.class);
    assertThat(savedEntity.getUserId(), is(MongoDbTestUtils.USER1_ID));
  }
}
