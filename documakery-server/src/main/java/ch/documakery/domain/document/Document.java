package ch.documakery.domain.document;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

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
  private String name;
  
  /**
   * Reference ids to the {@link DocumentBlock}s.
   */
  private List<ObjectId> documentBlockIds = new ArrayList<>();
  
//  /**
//   * Cached {@link DocumentBlock}s, iteration order of the map will match the order of the
//   * {@link DocumentBlock} ids.
//   */
//  @Transient
//  @JsonIgnore
//  private Map<ObjectId, DocumentBlock> documentBlockCache;
  
  /**
   * Reference id to the user, i.e. owner.
   */
  @Indexed
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

//  /**
//   * Sets the {@link DocumentBlock}s. Only the ids are saved to the database, but
//   * the specified {@link DocumentBlock}s are cached.
//   * 
//   * @param documentBlocks
//   */
//  public void setDocumentBlocks(List<DocumentBlock> documentBlocks) {
//    // New list for the ids
//    this.documentBlockIds = new ArrayList<ObjectId>();
//    
//    // New map for the cache
//    this.documentBlockCache = new HashMap<ObjectId, DocumentBlock>();
//    
//    if (documentBlocks != null) {
//      for (DocumentBlock documentBlock : documentBlocks) {
//        ObjectId id = documentBlock.getId();
//        
//        // Add to the keys
//        this.documentBlockIds.add(id);
//        
//        // Add to the cache
//        this.documentBlockCache.put(id, documentBlock);
//      }
//    }
//  }
  
//  /**
//   * Returns the referenced {@link DocumentBlock}s of this {@link Document}.
//   * If the cache is empty they are lazy-loaded from the datastore.
//   * 
//   * @return
//   */
//  public List<DocumentBlock> getDocumentBlocks() {
//    if (documentBlockCache == null) {
//      // lazy loading and saving to cache
//      if (documentBlockIds != null) {
//        // TODO: load from db
////        documentBlockCache = ofy().get(documentBlockIds);
//      } else {
//        documentBlockCache = new HashMap<ObjectId, DocumentBlock>();
//      }
//    }
//    return new ArrayList<DocumentBlock>(documentBlockCache.values());
//  }
  
  public ObjectId getUserId() {
    return userId;
  }

  public void setUserId(ObjectId userId) {
    this.userId = userId;
  }
}
