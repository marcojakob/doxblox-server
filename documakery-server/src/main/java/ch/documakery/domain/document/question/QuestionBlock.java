package ch.documakery.domain.document.question;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import ch.documakery.domain.document.DocumentBlock;

/**
 * The domain object for a block of {@link Question}s. 
 */
@Document
public class QuestionBlock implements DocumentBlock {

  /**
   * The type of library for a {@link QuestionBlock}.
   */
  public enum LibraryType {
    /**
     * Private library only visible to the owner.
     */
    PRIVATE,
    
    /**
     * Public library visible to all.
     */
    PUBLIC
  }
  
  @Id
  private ObjectId id;
  
  /**
   * The title of the question block.
   */
  @NotBlank
  @Size(max = 120)
  private String title;
  
  /**
   * An (optional) introduction. Is mainly used if a {@link QuestionBlock} has
   * multiple questions.
   */
  private String introduction;
  
  /**
   * An (embedded) list of {@link Question}s.
   */
  private List<Question> questions = new ArrayList<>();
  
  /**
   * An (embedded) list of {@link Topics}. It may be more than
   * one because each {@link Question} could have a different topic.
   */
  private List<Topic> topics = new ArrayList<>();
  
  /**
   * The library this {@link QuestionBlock} is part of. Default is 
   * {@link LibraryType#PRIVATE}.
   */
  @NotNull
  private LibraryType libraryType = LibraryType.PRIVATE;
  
  /**
   * The popularity.
   */
  private int popularity; // initially 0
  
  /**
   * Reference to the user, i.e. owner.
   */
  private ObjectId userId;

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public String getIntroduction() {
    return introduction;
  }

  public void setIntroduction(String introduction) {
    this.introduction = introduction;
  }

  public List<Question> getQuestions() {
    return questions;
  }

  public void setQuestions(List<Question> questions) {
    this.questions = questions;
  }

  public List<Topic> getTopics() {
    return topics;
  }

  public void setTopics(List<Topic> topics) {
    this.topics = topics;
  }
  
  public LibraryType getLibraryType() {
    return libraryType;
  }

  public void setLibraryType(LibraryType libraryType) {
    this.libraryType = libraryType;
  }

  public int getPopularity() {
    return popularity;
  }

  public void setPopularity(int popularity) {
    this.popularity = popularity;
  }

  public ObjectId getUserId() {
    return userId;
  }

  public void setUserId(ObjectId userId) {
    this.userId = userId;
  }
}
