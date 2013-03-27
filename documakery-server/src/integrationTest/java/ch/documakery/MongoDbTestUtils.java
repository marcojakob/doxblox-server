package ch.documakery;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Base64;

import ch.documakery.domain.document.DocumentFolder;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;


/**
 * MongoDB related utility class used for testing.
 * 
 * @author Marco Jakob
 */
public class MongoDbTestUtils {
  
  public static final ObjectId USER1_ID = new ObjectId("111111111111111111111111");
  public static final String USER1_EMAIL = "user1@example.com";
  public static final String USER1_NICKNAME = "nicknameUser1";
  public static final String USER1_PASSWORD = "p@sswördUser1";
  
  public static final ObjectId USER2_ID = new ObjectId("222222222222222222222222");
  public static final String USER2_EMAIL = "user2@example.com";
  public static final String USER2_NICKNAME = "nicknameUser2";
  public static final String USER2_PASSWORD = "p#sswordUser2";
  
  
  /**
   * Removes all entries in all the collections of the db.
   * 
   * @param template The template of the db
   */
  public static void cleanDb(MongoTemplate template) {
    for (String collectionName : template.getCollectionNames()) {
      template.remove(new Query(), collectionName);
    }
  }
  
  
  /**
   * Imports user test data including {@link #USER1_EMAIL} and {@link #USER2_EMAIL}.
   * 
   * @param template
   * @throws IOException
   */
  public static void importTestUsers(MongoTemplate template) throws IOException {
    importData(Thread.currentThread().getContextClassLoader().getResourceAsStream("test-data/users.json"), 
        template.getCollection("user"));
  }
  
  /**
   * Imports test data.
   * 
   * @param template
   * @throws IOException
   */
  public static void importTestDocumentFolders(MongoTemplate template) throws IOException {
    importData(Thread.currentThread().getContextClassLoader().getResourceAsStream("test-data/documentFolders.json"), 
        template.getCollection("documentFolder"));
  }
  
  /**
   * Imports test data.
   * 
   * @param template
   * @throws IOException
   */
  public static void importTestDocuments(MongoTemplate template) throws IOException {
    importData(Thread.currentThread().getContextClassLoader().getResourceAsStream("test-data/documents.json"), 
        template.getCollection("document"));
  }
  
  /**
   * Imports test data.
   * 
   * @param template
   * @throws IOException
   */
  public static void importTestQuestionBlocks(MongoTemplate template) throws IOException {
    importData(Thread.currentThread().getContextClassLoader().getResourceAsStream("test-data/questionBlocks.json"), 
        template.getCollection("questionBlock"));
  }
  
  public static void importGeneratedTestData(MongoTemplate template, ObjectId userId) throws IOException {
    List<Object> result = new ArrayList<>();
    DocumentFolder root = TestDataFactory.createFolder("root", userId, result);
    TestDataFactory.createFolderTreeBwl(root, userId, result);
    TestDataFactory.createFolderTreeRecht(root, userId, result);
    TestDataFactory.createFolderTreeRw(root, userId, result);
    
    for (Object object : result) {
      template.save(object);
    }
  }
  
  
  
  /**
   * Imports data into a collection.
   * 
   * @param jsonStream The data to import as JSON Input Stream
   * @param collection The collection where the JSON should be imported.
   * @throws IOException 
   */
  public static void importData(InputStream jsonStream, DBCollection collection) throws IOException {
    try (InputStreamReader stream = new InputStreamReader(jsonStream, Charsets.UTF_8)) {
      String jsonString = CharStreams.toString(stream);
      importData(jsonString, collection);
    }
  }
  
  /**
   * Imports data into a collection.
   * 
   * @param json The data to import as JSON String
   * @param collection The collection where the JSON should be imported.
   */
  @SuppressWarnings("unchecked")
  public static void importData(String json, DBCollection collection) {
    Object parsedJson = JSON.parse(json);
    if (parsedJson instanceof List<?>) {
      collection.insert((List<DBObject>) parsedJson);
    } else if (parsedJson instanceof DBObject) {
      collection.insert((DBObject) parsedJson);
    }
  }
  
  /**
   * Helper method to serialize a collection into its JSON form.
   * 
   * @param template 
   * @param collectionName
   * @param prettyPrint
   * @return String containing JSON form of the object
   */
  public static String convertToJsonString(MongoTemplate template, String collectionName, boolean prettyPrint) throws IOException {
    DBCollection collection = template.getCollection(collectionName);
    return convertToJsonString(collection.find(), true);
  }

  /**
   * Helper method to serialize a {@link DBObject} into its JSON form.
   * 
   * @param dbObject object to serialize
   * @param prettyPrint
   * @return String containing JSON form of the object
   */
  public static String convertToJsonString(DBObject dbObject, boolean prettyPrint) throws IOException {
    return doConvertToJsonString(dbObject, prettyPrint);
  }
  
  /**
   * Helper method to serialize multiple {@link DBObject}s into their JSON form.
   * Use e.g. with a {@link DBCursor} from {@link DBCollection#find()}.
   * 
   * @param dbObjects object to serialize
   * @param prettyPrint
   * @return String containing JSON form of the object
   */
  public static String convertToJsonString(Iterable<DBObject> dbObjects, boolean prettyPrint) throws IOException {
    return doConvertToJsonString(dbObjects, prettyPrint);
  }
  
  private static String doConvertToJsonString(Object object, boolean prettyPrint) throws IOException {
    String json = JSON.serialize(object);
    if (prettyPrint) {
      json = JsonTestUtils.prettyPrintJson(json);
    }
    return json;
  }
  
  /**
   * Helper method to create HTTP Basic Authentication Header String.
   * 
   * @param username
   * @param password
   * @return
   */
  public static String createBasicAuthHeader(String username, String password) {
    String authorization = username + ":" + password;
    String basic = new String(Base64.encode(authorization.getBytes(Charset.forName("UTF-8"))));
    return "Basic " + basic;
  }
  
  /**
   * Hashes the specified password with BCrypt. The strength is the default (10), see
   * {@link BCrypt#GENSALT_DEFAULT_LOG2_ROUNDS}.
   * 
   * @param rawPassword
   * @return
   */
  public static String hashWithBCrypt(String rawPassword) {
    return new BCryptPasswordEncoder().encode(rawPassword);
  }
}
