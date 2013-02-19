package ch.documakery.domain.document.question;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * The domain object for a question.
 * 
 * @author Marco Jakob
 */
@Document(collection="question")
public abstract class Question {
  
  @Id
  private ObjectId id;

  /**
   * The actual text of the question.
   */
  private String text;
  
  /**
   * Notes on how to correct this question.
   */
  private String correctionNotes;
  
  /**
   * The maximum points one can get when this question is answered correctly.
   */
  private int points; 
  
  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getCorrectionNotes() {
    return correctionNotes;
  }

  public void setCorrectionNotes(String correctionNotes) {
    this.correctionNotes = correctionNotes;
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }
}
