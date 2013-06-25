package ch.doxblox.repository;

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

import ch.doxblox.MongoDbTestUtils;
import ch.doxblox.domain.document.question.QuestionBlock;
import ch.doxblox.repository.QuestionBlockRepository;

/**
 * Test class for {@link QuestionBlockRepository}.
 * 
 * @author Marco Jakob
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:META-INF/spring/applicationContext-*.xml"})
@ActiveProfiles("dev")
@WebAppConfiguration
public class QuestionBlockRepositoryIntegrationTest {
  
  @Inject
  QuestionBlockRepository repository; // SUT
  @Inject
  MongoTemplate template;

  @Before
  public void setUp() {
    MongoDbTestUtils.cleanDb(template);
  }
  
  @Test
  public void findByUserId_User1_ReturnsTwoDocuments() throws Exception {
    // given
    MongoDbTestUtils.importTestQuestionBlocks(template);
    assertThat(repository.findAll().size(), is(3));

    // when
    List<QuestionBlock> foundDocsUser1 = repository.findByUserId(MongoDbTestUtils.USER1_ID);
    List<QuestionBlock> foundDocsUser2 = repository.findByUserId(MongoDbTestUtils.USER2_ID);
    
    // then
    assertThat(foundDocsUser1.size(), is(3));
    assertThat(foundDocsUser1.get(0).getTitle(), is("Inventar 1"));
    assertThat(foundDocsUser2.size(), is(0));
  }
}
