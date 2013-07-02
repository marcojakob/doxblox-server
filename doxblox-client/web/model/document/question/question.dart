part of doxblox.model;

/**
 * The model object for a question, always embedded inside a [QuestionBlock].
 */
abstract class Question {
  String text;
  int points;
  
  Question();
  
  /**
   * Factory constructor creating instances of [Question] subclasses depending
   * on the 'type' in the [jsonMap].
   */
  factory Question.fromJson(Map jsonMap) {
    String type = jsonMap['type'];
    switch(type) {
      case TextQuestion.TYPE: 
        return new TextQuestion.fromJson(jsonMap);
      default: 
        throw new ArgumentError('JSON Map does not contain a valid "type" field. The "type" must be a subclass of Question.');
    }
  }
}