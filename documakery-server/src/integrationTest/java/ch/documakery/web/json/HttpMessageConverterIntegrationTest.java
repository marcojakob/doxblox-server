package ch.documakery.web.json;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import ch.documakery.JsonTestUtils;

/**
 * Integration test for {@link MyObjectMapper} and it's usage as mvc:message-converters.
 * 
 * @author Marco Jakob
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:META-INF/spring/applicationContext-bootstrap.xml"})
@ActiveProfiles("dev")
@WebAppConfiguration
public class HttpMessageConverterIntegrationTest {
  
  @Inject
  private RequestMappingHandlerAdapter mapping;
  
  @Test
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void serialize() throws Exception {
    // given
    ObjectId id = new ObjectId("51267930f5c7f925eaae04d3");

    // when
    HttpOutputMessage outputMessage = new MockHttpOutputMessage();
    
    // Find first matching converter and convert
    List<HttpMessageConverter<?>> converters = mapping.getMessageConverters();
    for (HttpMessageConverter converter : converters) {
      if (converter.canWrite(ObjectId.class, JsonTestUtils.APPLICATION_JSON_UTF8)) {
        converter.write(id, JsonTestUtils.APPLICATION_JSON_UTF8, outputMessage);
        break;
      }
    }
    
    String convertedId = outputMessage.getBody().toString();
    
    // then
    assertThat(convertedId, is("\"51267930f5c7f925eaae04d3\""));
  }
  
  @Test
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void deserialize() throws Exception {
    // given
    String id = "\"51267930f5c7f925eaae04d3\"";
    
    // when
    HttpInputMessage inputMessage = new MockHttpInputMessage(id.getBytes());
    ObjectId convertedId = null;
    
    // Find first matching converter and convert
    List<HttpMessageConverter<?>> converters = mapping.getMessageConverters();
    for (HttpMessageConverter converter : converters) {
      if (converter.canRead(ObjectId.class, JsonTestUtils.APPLICATION_JSON_UTF8)) {
        convertedId = (ObjectId) converter.read(ObjectId.class, inputMessage);
        break;
      }
    }
    
    // then
    assertThat(convertedId, is(new ObjectId("51267930f5c7f925eaae04d3")));
  }
}
