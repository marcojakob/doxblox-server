package com.documakery.domain.document;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

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
  private String name = "No Name";
  
  /**
   * The creation date.
   */
  private Date creationDate = new Date();
  
  /**
   * Reference ids to the {@link DocumentBlock}s.
   */
  private List<ObjectId> documentBlockIds;
  
  /**
   * Cached {@link DocumentBlock}s, iteration order of the map will match the order of the
   * {@link DocumentBlock} keys.
   */
  @Transient
  private Map<ObjectId, DocumentBlock> documentBlockCache;
  
  /**
   * Reference id to the user, i.e. owner.
   */
  private ObjectId userId;

  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public Date getCreationDate() {
    return creationDate;
  }
  
  public List<ObjectId> getDocumentBlockIds() {
    return documentBlockIds;
  }

  public void setDocumentBlockIds(List<ObjectId> documentBlockIds) {
    this.documentBlockIds = documentBlockIds;
  }

  /**
   * Sets the {@link DocumentBlock}s. Only the ids are saved to the database, but
   * the specified {@link DocumentBlock}s are cached.
   * 
   * @param documentBlocks
   */
  public void setDocumentBlocks(List<DocumentBlock> documentBlocks) {
    // New list for the ids
    this.documentBlockIds = new ArrayList<ObjectId>();
    
    // New map for the cache
    this.documentBlockCache = new HashMap<ObjectId, DocumentBlock>();
    
    if (documentBlocks != null) {
      for (DocumentBlock documentBlock : documentBlocks) {
        ObjectId id = documentBlock.getId();
        
        // Add to the keys
        this.documentBlockIds.add(id);
        
        // Add to the cache
        this.documentBlockCache.put(id, documentBlock);
      }
    }
  }
  
  /**
   * Returns the referenced {@link DocumentBlock}s of this {@link Document}.
   * If the cache is empty they are lazy-loaded from the datastore.
   * 
   * @return
   */
  public List<DocumentBlock> getDocumentBlocks() {
    if (documentBlockCache == null) {
      // lazy loading and saving to cache
      if (documentBlockIds != null) {
        // TODO: load from db
//        documentBlockCache = ofy().get(documentBlockIds);
      } else {
        documentBlockCache = new HashMap<ObjectId, DocumentBlock>();
      }
    }
    return new ArrayList<DocumentBlock>(documentBlockCache.values());
  }
  
  public ObjectId getUserId() {
    return userId;
  }

  public void setUserId(ObjectId userId) {
    this.userId = userId;
  }
}
