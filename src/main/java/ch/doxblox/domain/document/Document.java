package ch.doxblox.domain.document;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The datastore object for a document.
 */
@org.springframework.data.mongodb.core.mapping.Document
public class Document {
  
  @Id
  private ObjectId id;
  
  /**
   * The name.
   */
  @NotBlank
  @Size(max = 80)
  private String name;
  
  /**
   * Reference ids to the {@link DocumentBlock}s.
   */
  private List<ObjectId> documentBlockIds = new ArrayList<>();
  
  /**
   * Reference id to the user, i.e. owner.
   */
  @Indexed
  @JsonIgnore
  private ObjectId userId;
  
  public ObjectId getId() {
    return id;
  }
  
  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public List<ObjectId> getDocumentBlockIds() {
    return documentBlockIds;
  }

  public void setDocumentBlockIds(List<ObjectId> documentBlockIds) {
    this.documentBlockIds = documentBlockIds;
  }

  public ObjectId getUserId() {
    return userId;
  }

  public void setUserId(ObjectId userId) {
    this.userId = userId;
  }
  
  @Override
  public String toString() {
    return this.name;
  }
}
