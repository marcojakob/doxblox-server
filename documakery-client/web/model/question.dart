part of model;

/**
 * Model object for a block of [Question]s.
 */
class QuestionBlock implements DocumentBlock {
  String id;
  String title;
  String introduction;
  List<Question> questions;
  List<String> topics;
  String libraryType;
  String userId;
  
  /**
   * Default constructor.
   */
  QuestionBlock();
  
  /**
   * Constructs a [QuestionBlock] from a JSON String.
   */
  QuestionBlock.fromJson(String jsonString) {
    var obj = json.parse(jsonString);
    id = obj['id'];
    title = obj['title'];
    introduction = obj['introduction'];
    questions = obj['questions'];
    topics = obj['topics'];
    libraryType = obj['libraryType'];
    userId = obj['userId'];
  }
  
  /**
   * Converts this object to a JSON map.
   */
  Map toJson() {
    return {
      'id': id,
      'title': title, 
      'introduction': introduction,
      'questions': questions,
      'topics': topics,
      'libraryType': libraryType,
      'userId': userId
    };
  }
  
  String toString() {
    return json.stringify(this);
  }
  
  String get digestTitle {
    return trimToSize(title, 50);
  }
  
  String get digestSnippet {
    return trimToSize(introduction, 100);
  }
}

/**
 * The model object for a question, always embedded inside a [QuestionBlock].
 */
abstract class Question {
  String text;
  String correctionNotes;
  int points;
}

/**
 * The model object for a question with a simple text answer.
 */
class TextAnswerQuestion extends Question {
  int emptyAnswerLines;
  
  /**
   * Default constructor.
   */
  TextAnswerQuestion();
  
  /**
   * Constructs a [TextAnswerQuestion] from a JSON String.
   */
  TextAnswerQuestion.fromJson(String jsonString) {
    var obj = json.parse(jsonString);
    text = obj['text'];
    correctionNotes = obj['correctionNotes'];
    points = obj['points'];
    emptyAnswerLines = obj['emptyAnswerLines'];
  }
  
  /**
   * Converts this object to a JSON map.
   */
  Map toJson() {
    return {
      'text': text,
      'correctionNotes': correctionNotes, 
      'points': points,
      'emptyAnswerLines': emptyAnswerLines
    };
  }
  
  String toString() {
    return json.stringify(this);
  }
}