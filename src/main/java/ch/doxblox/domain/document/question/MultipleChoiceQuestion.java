package ch.doxblox.domain.document.question;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The domain object for a mulitple choice question.
 */
@Document
public class MultipleChoiceQuestion extends Question {

  /**
   * A list of possible answers.
   */
  private List<MultipleChoiceAnswer> answers = new ArrayList<>();
  
  public List<MultipleChoiceAnswer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<MultipleChoiceAnswer> answers) {
    this.answers = answers;
  }
}
