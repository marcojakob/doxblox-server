package ch.documakery.domain.document;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The domain object for a folder that contains documents or other folders. 
 * </p> 
 * The folders are hierarchical. Every folder contains a reference to its parent
 * folder or <code>null</code> if it is a root folder. 
 * 
 * @author Marco Jakob
 */
// TODO: might require compound index on (userId, parentId)
@org.springframework.data.mongodb.core.mapping.Document
public class DocumentFolder {
  
  @Id
  private ObjectId id;
  
  /**
   * The name of the folder.
   */
  @NotBlank
  @Size(max = 50)
  private String name;

  /**
   * Reference to the parent folder or <code>null</code> if it is the root.
   */
  @Indexed
  private ObjectId parentId;
  
  /**
   * Reference ids to the {@link Document}s inside this folder.
   */
  private List<ObjectId> documentIds = new ArrayList<>();
  
  /**
   * Reference id to the user, i.e. owner.
   */
  @Indexed
  @JsonIgnore
  private ObjectId userId;

  @SuppressWarnings("unused")
  private DocumentFolder() {
    // For serialization only
  }

  /**
   * Constructor.
   * 
   * @param name
   */
  public DocumentFolder(String name) {
    this.name = name;
  }
  
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

  public ObjectId getParentId() {
    return parentId;
  }

  public void setParentId(ObjectId parentId) {
    this.parentId = parentId;
  }

  public List<ObjectId> getDocumentIds() {
    return documentIds;
  }

  public void setDocumentIds(List<ObjectId> childDocumentIds) {
    this.documentIds = childDocumentIds;
  }

  public ObjectId getUserId() {
    return userId;
  }

  public void setUserId(ObjectId userId) {
    this.userId = userId;
  }

  @Override
  public String toString() {
    return "Folder [" + name + "]";
  }
}
