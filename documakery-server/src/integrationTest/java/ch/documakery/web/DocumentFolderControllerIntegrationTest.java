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
import ch.documakery.domain.document.DocumentFolder;
import ch.documakery.repository.DocumentFolderRepository;
import ch.documakery.repository.MongoDbTestUtils;

/**
 * Integration test for {@link DocumentFolderController}.
 * 
 * @author Marco Jakob
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:META-INF/spring/applicationContext-*.xml"})
@ActiveProfiles("dev")
@WebAppConfiguration
public class DocumentFolderControllerIntegrationTest {
  
  @Inject
  private FilterChainProxy springSecurityFilterChain;

  @Inject
  private WebApplicationContext webApplicationContext;

  @Inject
  private MongoTemplate template;
  
  @Inject
  private DocumentFolderRepository folderRepository;
  
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
  public void getAllFoldersOfUser_AsUser_ReturnFolders() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    MongoDbTestUtils.importTestDocumentFolders(template);
    
    // when
    mockMvc.perform(get("/documentfolders")
        .with(userDetailsService(MongoDbTestUtils.USER1_EMAIL))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
    )
    // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$[0].name", is("root")))
        .andExpect(jsonPath("$[1].name", is("2012 Herbstsemester")))
        .andExpect(jsonPath("$[2].name", is("B1.503")))
        .andExpect(jsonPath("$[5]").doesNotExist());
  }
  
  
  @Test
  public void getAllFoldersOfUser_AsUserWithNoDocuments_ReturnEmptyBody() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    // when
    mockMvc.perform(get("/documentfolders")
        .with(userDetailsService(MongoDbTestUtils.USER1_EMAIL))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
    )
    // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$").doesNotExist());
  }
  
  @Test
  public void getAllFoldersOfUser_AsAnonymous_ReturnUnauthorized() throws Exception {
    // when
    mockMvc.perform(get("/documentfolders")
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
    )
    // then
        .andExpect(status().isUnauthorized());
  }
  
  @Test
  public void createFolder_AsUser_ReturnOkAndFolderInDb() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    DocumentFolder folder = new DocumentFolder("Bla - Folder");
    
    // when
    mockMvc.perform(post("/documentfolders")
        .with(userDetailsService(MongoDbTestUtils.USER1_EMAIL))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        .content(JsonTestUtils.convertToJsonBytes(folder))
    )
    // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.name", is("Bla - Folder")));
    
    assertThat(folderRepository.count(), is(1L));
  }
  
  @Test
  public void createFolder_AsAnonymous_ReturnUnauthorizedAndFolderNotInDb() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    DocumentFolder folder = new DocumentFolder("Bla - Folder");
    
    // when
    mockMvc.perform(post("/documentfolders")
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        .content(JsonTestUtils.convertToJsonBytes(folder))
    )
    // then
        .andExpect(status().isUnauthorized());
    
    assertThat(folderRepository.count(), is(0L));
  }
  
  @Test
  public void createFolder_InvalidName_ReturnErrorAndDocumentNotInDb() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    DocumentFolder folder = new DocumentFolder(null);
    
    // when
    mockMvc.perform(post("/documentfolders")
        .with(userDetailsService(MongoDbTestUtils.USER1_EMAIL))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        .content(JsonTestUtils.convertToJsonBytes(folder))
    )
    // then
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.errors[0].path", is("name")))
        .andExpect(jsonPath("$.errors[0].message", is("may not be empty")))
        .andExpect(jsonPath("$.errors[1]").doesNotExist());
    
    assertThat(folderRepository.count(), is(0L));
  }
  
  @Test
  public void createFolder_IllegalUserIdSet_FolderSavedButUserIdIsIgnored() throws Exception {
    // given
    MongoDbTestUtils.importTestUsers(template);
    
    // when
    mockMvc.perform(post("/documentfolders")
        .with(userDetailsService(MongoDbTestUtils.USER1_EMAIL))
        .contentType(JsonTestUtils.APPLICATION_JSON_UTF8)
        .content("{\"id\":\"666666666666666666666666\",\"name\":\"folderName\",\"userId\":{\"$oid\":\"aaaaaaaaaaaaaaaaaaaaaaaa\"}}")
    )
    // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(JsonTestUtils.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id", is("666666666666666666666666")))
        .andExpect(jsonPath("$.userId").doesNotExist())
        .andExpect(jsonPath("$.name", is("folderName")));
    
    DocumentFolder savedEntity = template.findById("666666666666666666666666", DocumentFolder.class);
    assertThat(savedEntity.getUserId(), is(MongoDbTestUtils.USER1_ID));
  }
}
