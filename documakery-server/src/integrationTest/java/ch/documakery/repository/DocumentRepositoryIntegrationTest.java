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

import ch.documakery.domain.document.Document;

/**
 * Test class for {@link DocumentRepository}.
 * 
 * @author Marco Jakob
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:META-INF/spring/applicationContext-*.xml"})
@ActiveProfiles("dev")
@WebAppConfiguration
public class DocumentRepositoryIntegrationTest {
  
  @Inject
  DocumentRepository repository;
  @Inject
  MongoTemplate template;

  @Before
  public void setUp() {
    MongoDbTestUtils.cleanDb(template);
  }
  
  @Test
  public void findByUserId_User1_Return2Documents() throws Exception {
    // given
    MongoDbTestUtils.importTestDocuments(template);
    assertThat(repository.findAll().size(), is(3));

    // when
    List<Document> foundDocs = repository.findByUserId(MongoDbTestUtils.USER1_ID);
    
    // then
    assertThat(foundDocs.size(), is(2));
    assertThat(foundDocs.get(0).getName(), is("Prüfung - Inventar, Bilanz, Bilanzveränderung"));
  }
}
