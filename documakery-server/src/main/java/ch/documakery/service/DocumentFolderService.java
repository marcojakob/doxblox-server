package ch.documakery.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import ch.documakery.domain.document.DocumentFolder;


/**
 * Service for managing {@link DocumentFolder}s.
 * 
 * @author Marco Jakob
 */
public interface DocumentFolderService {
  
  /**
   * Returns all {@link DocumentFolder}s of the current user.
   * 
   * @return
   */
  @PreAuthorize("isAuthenticated()")
  public List<DocumentFolder> getAllFoldersOfUser();
  
  /**
   * Adds the current logged in user to the {@link DocumentFolder} and saves it.
   * </p>
   * If the entity does not have an id, it will automatically be set.
   * 
   * @param folder The folder to be saved.
   * @return the saved entity
   */
  @PreAuthorize("isAuthenticated()")
  public DocumentFolder saveWithUser(DocumentFolder folder);
}
