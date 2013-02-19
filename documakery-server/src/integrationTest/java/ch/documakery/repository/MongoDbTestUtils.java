package ch.documakery.repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import ch.documakery.JsonTestUtils;
import ch.documakery.domain.user.User;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
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
  
  public static final String CORRECT_USERNAME = "nicki99@example.com";
  public static final String CORRECT_NICKNAME = "nicki99";
  public static final String CORRECT_PASSWORD = "nicki99pass";
  public static final String CORRECT_PASSWORD_BCRYPT = "$2a$10$eVNlYxxVtBSX3eWZ1XGKQ.kCXU32umMMilmcc9CnmdTj62AkSSkiK";
  public static final String INCORRECT_EMAIL = "user-incorrect@user.com";
  public static final String INCORRECT_PASSWORD = "password-incorrect";

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
   * Adds the user with correct credentials {@link #CORRECT_USERNAME} and {@link #CORRECT_PASSWORD}
   * to the db.
   * 
   * @param template
   */
  public static void addCorrectUserToDb(MongoTemplate template) {
    User user = new User(CORRECT_USERNAME);
    user.setNickname(CORRECT_NICKNAME);
    user.setPassword(CORRECT_PASSWORD_BCRYPT);
    template.save(user);
  }
  
  /**
   * Imports data into a collection.
   * 
   * @param jsonStream The data to import as JSON Input Stream
   * @param collection The collection where the JSON should be imported.
   * @throws IOException 
   */
  public static void importData(InputStream jsonStream, DBCollection collection) throws IOException {
    String jsonString = CharStreams.toString(new InputStreamReader(jsonStream, Charsets.UTF_8));
    Closeables.closeQuietly(jsonStream);
    importData(jsonString, collection);
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
}
