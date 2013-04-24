package ch.documakery.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import ch.documakery.domain.document.question.QuestionBlock;


/**
 * Service for managing {@link QuestionBlock}s.
 * 
 * @author Marco Jakob
 */
public interface QuestionBlockService {
  
  /**
   * Returns all {@link QuestionBlock}s of the current user.
   * 
   * @return
   */
  @PreAuthorize("isAuthenticated()")
  public List<QuestionBlock> getAllQuestionBlocksOfUser();
  
  /**
   * Adds the current logged in user to the {@link QuestionBlock} and saves it.
   * </p>
   * If the entity does not have an id, it will automatically be set.
   * 
   * @param questionBlock The document to be saved.
   * @return the saved entity
   */
  @PreAuthorize("isAuthenticated()")
  public QuestionBlock saveWithUser(QuestionBlock questionBlock);
}
