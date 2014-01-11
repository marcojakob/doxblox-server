package ch.doxblox;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * JSON related utility class used for testing.
 * 
 * @author Marco Jakob
 */
public class JsonTestUtils {

  /**
   * {@link MediaType} used to set the content type and character set of HTTP requests and to verify
   * the content type and character set of HTTP responses.
   */
  public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON
      .getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

  /**
   * Converts the given object to bytes that contain the JSON representation of the object.
   * 
   * @param object
   * @return
   * @throws IOException
   */
  public static byte[] convertToJsonBytes(Object object) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
    return mapper.writeValueAsBytes(object);
  }

  /**
   * Pretty prints the specified JSON String.
   * 
   * @param json
   * @return
   * @throws IOException
   */
  public static String prettyPrintJson(String json) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
  
    ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
  
    JsonNode df = mapper.readValue(json, JsonNode.class);
    return writer.writeValueAsString(df);
  }
}
