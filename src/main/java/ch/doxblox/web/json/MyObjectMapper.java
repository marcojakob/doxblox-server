package ch.doxblox.web.json;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * ObjectMapper used for serializing MongoDb's ObjectId.
 * The serialization is used to transfer ObjectIds to the client.
 * 
 * @author Marco Jakob
 */
public class MyObjectMapper extends ObjectMapper {
  
  private static final long serialVersionUID = 1L;

  public MyObjectMapper() {
    SimpleModule module = new SimpleModule("ObjectIdModule");
    module.addSerializer(ObjectId.class, new ObjectIdSerializer());
    this.registerModule(module);
  }
}