package ch.doxblox.domain.document.question;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The domain object for a question with a simple text answer.
 */
@Document
public class TextAnswerQuestion extends Question {

  /**
   * Number of empty lines used to write an answer.
   */
  private int emptyAnswerLines;
  
  /**
   * The solution to this question.
   */
  private String solution;
  
  public int getEmptyAnswerLines() {
    return emptyAnswerLines;
  }

  public void setEmptyAnswerLines(int emptyAnswerLines) {
    this.emptyAnswerLines = emptyAnswerLines;
  }

  public String getSolution() {
    return solution;
  }

  public void setSolution(String solution) {
    this.solution = solution;
  }
}
