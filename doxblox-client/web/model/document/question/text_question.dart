part of doxblox.model;

/**
 * The model object for a question with a simple text answer.
 */
class TextQuestion extends Question {
  static const String TYPE = 'TextQuestion';
  
  String solution;
  int emptyAnswerLines;
  
  /**
   * Default constructor.
   */
  TextQuestion();
  
  /**
   * Constructs a [TextQuestion] from a the [jsonMap].
   */
  TextQuestion.fromJson(Map jsonMap) {
    text = jsonMap['text'];
    points = jsonMap['points'];
    emptyAnswerLines = jsonMap['emptyAnswerLines'];
    solution = jsonMap['solution'];
  }
  
  @override
  Map toJson() {
    return {
      'text': text,
      'points': points,
      'emptyAnswerLines': emptyAnswerLines,
      'solution': solution,
      'type' : TYPE
    };
  }
  
  String toString() {
    return JSON.encode(this);
  }
}