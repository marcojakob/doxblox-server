package ch.documakery.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import ch.documakery.domain.document.Document;


/**
 * Service for managing {@link Document}s.
 * 
 * @author Marco Jakob
 */
public interface DocumentService {
  
  /**
   * Returns all {@link Document}s of the current user.
   * 
   * @return
   */
  @PreAuthorize("isAuthenticated()")
  public List<Document> getAllDocumentsOfUser();
  
  /**
   * Adds the current logged in user to the {@link Document} and saves it.
   * </p>
   * If the entity does not have an id, it will automatically be set.
   * 
   * @param document The document to be saved.
   * @return the saved entity
   */
  @PreAuthorize("isAuthenticated()")
  public Document saveWithUser(Document document);
}
