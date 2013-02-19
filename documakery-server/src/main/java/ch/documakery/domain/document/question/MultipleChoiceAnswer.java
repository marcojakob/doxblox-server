package ch.documakery.domain.document.question;

/**
 * The multiple choice answer is used as answers for
 * {@link MultipleChoiceQuestion}s.
 * 
 * @author Marco Jakob
 */
public class MultipleChoiceAnswer {

  private String answer;
  private boolean isCorrect;
  private String solution;

  public MultipleChoiceAnswer() {
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public boolean isCorrect() {
    return isCorrect;
  }

  public void setIsCorrect(boolean isCorrect) {
    this.isCorrect = isCorrect;
  }

  public String getSolution() {
    return solution;
  }

  public void setSolution(String solution) {
    this.solution = solution;
  }
}
