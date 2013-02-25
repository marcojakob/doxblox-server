package ch.documakery.repository;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

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
import ch.documakery.domain.document.DocumentFolder;

/**
 * Test class for {@link DocumentFolderRepository}.
 * 
 * @author Marco Jakob
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:META-INF/spring/applicationContext-*.xml"})
@ActiveProfiles("dev")
@WebAppConfiguration
public class DocumentFolderRepositoryIntegrationTest {
  
  @Inject
  DocumentFolderRepository repository;
  @Inject
  MongoTemplate template;

  @Before
  public void setUp() {
    MongoDbTestUtils.cleanDb(template);
  }
  
  @Test
  public void findByUserId_User1_Return5Folders() throws Exception {
    // given
    MongoDbTestUtils.importTestDocumentFolders(template);
    assertThat(repository.findAll().size(), is(7));

    // when
    List<DocumentFolder> folders = repository.findByUserId(MongoDbTestUtils.USER1_ID);
    
    // then
    assertThat(folders.size(), is(5));
  }
}
