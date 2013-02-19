package ch.documakery.domain.document.question;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * The domain object for a {@link Topic}. 
 */
public class Topic {

  @Id
  private ObjectId id;
  
  /**
   * The name of the topic.
   */
  @Indexed(unique=true)
  private String name;
  
  /**
   * Reference to a parent {@link Topic}. May be <code>null</code> if it is 
   * a root {@link Topic}.
   */
  private ObjectId parentTopicId;
  
  /**
   * Creates a {@link Topic} with specified name.
   * 
   * @param name
   */
  public Topic(String name) {
    this.name = name;
  }
  
  public String getName() {
    return name;
  }

  public ObjectId getParentTopicId() {
    return parentTopicId;
  }

  public void setParentTopicId(ObjectId parentTopicId) {
    this.parentTopicId = parentTopicId;
  }
}
