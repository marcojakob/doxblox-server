package ch.documakery.domain.document.question;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The domain object for a mulitple choice question.
 */
@Document(collection="question")
public class MultipleChoiceQuestion extends Question {

  /**
   * A list of possible answers.
   */
  private List<MultipleChoiceAnswer> answers;
  
  public List<MultipleChoiceAnswer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<MultipleChoiceAnswer> answers) {
    this.answers = answers;
  }
}
