part of doxblox.model;

/**
 * Model object for a block of [Question]s.
 */
class QuestionBlock extends DocumentBlock {
  String id;
  String title;
  String introduction;
  List<Question> questions;
  String subject;
  String topic;
  String library;
  String userId;
  
  /**
   * Default constructor.
   */
  QuestionBlock();
  
  /**
   * Constructs a [QuestionBlock] from the [jsonMap].
   */
  QuestionBlock.fromJson(Map jsonMap) {
    id = jsonMap['id'];
    title = jsonMap['title'];
    introduction = jsonMap['introduction'];

    questions = new List<Question>();
    if (jsonMap['questions'] != null) {
      for (Map questionMap in jsonMap['questions']) {
        questions.add(new Question.fromJson(questionMap)); 
      }
    }
    
    subject = jsonMap['subject'];
    topic = jsonMap['topic'];
    library = jsonMap['library'];
    userId = jsonMap['userId'];
  }
  
  Map toJson() {
    return {
      'id': id,
      'title': title, 
      'introduction': introduction,
      'questions': questions,
      'subject': subject,
      'topic': topic,
      'library': library,
      'userId': userId
    };
  }
  
  String toString() {
    return JSON.encode(this);
  }
  
  /**
   * Returns a shortened title.
   */
  String get digestTitle {
    return trimToSize(title, 50);
  }
  
  /**
   * Returns shortened content for the digest.
   */
  String get digestSnippet {
    return trimToSize(introduction, 100);
  }
}