package ch.documakery.repository;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
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
import ch.documakery.repository.DocumentRepository;

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
  
  static final String JSON_DATA_FILE = DocumentRepositoryIntegrationTest.class.getSimpleName() + "-data.json";
  static final String COLLECTION_NAME = "document";

  @Inject
  DocumentRepository repository;
  @Inject
  MongoTemplate template;

  @Before
  public void setUp() {
    MongoDbTestUtils.cleanDb(template);
  }
  
  @Test
  public void testSimplePersist() throws IOException {
    Document d = new Document();
    d.setName("test document");

    repository.save(d);

    List<Document> result = repository.findAll();

    assertThat(result.size(), is(1));
    assertThat(result.get(0).getName(), is("test document"));
  }

  @Test
  public void testReadJsonFromFile() throws IOException {
    MongoDbTestUtils.importData(getClass().getResourceAsStream(JSON_DATA_FILE), 
        template.getCollection("myCollection"));

    System.out.println(MongoDbTestUtils.convertToJsonString(
        template.getCollection("myCollection").find(), true));
  }

//  /**
//   * This tests based on an imported JSON data file.
//   */
//  @Test
//  public void testImport() throws Exception {
//    // perform operations
//    final DBCollection coll = db.getCollection("testCollection");
//    importCollection(coll, Thread.currentThread().getContextClassLoader().getResourceAsStream(
//        "sample.json"));
//
//    final DBObject myDoc = coll.findOne(new BasicDBObject("name", "MongoDB"));
//    assertEquals("MongoDB", myDoc.get("name"));
//    assertEquals(203, ((BasicDBObject) myDoc.get("info")).get("x"));
//
//    final DBObject myDoc = coll.findOne(new BasicDBObject("name", "Cassandra"));
//    assertEquals("Cassandra", myDoc.get("name"));
//    assertEquals(201, ((BasicDBObject) myDoc.get("info")).get("x"));
//  }
//
//  /**
//   * This is an example based on http://www.mongodb.org/display/DOCS/Java+Tutorial to see if things
//   * work.
//   */
//  @Test
//  public void testSample() throws Exception {
//
//    // perform operations
//    final DBCollection coll = db.getCollection("testCollection");
//
//    final BasicDBObject doc = new BasicDBObject();
//
//    doc.put("name", "MongoDB");
//    doc.put("type", "database");
//    doc.put("count", 1);
//
//    final BasicDBObject info = new BasicDBObject();
//
//    info.put("x", 203);
//    info.put("y", 102);
//
//    doc.put("info", info);
//
//    coll.insert(doc);
//
//    final DBObject myDoc = coll.findOne();
//    assertEquals("MongoDB", myDoc.get("name"));
//    assertEquals(203, ((BasicDBObject) myDoc.get("info")).get("x"));
//  }
}
