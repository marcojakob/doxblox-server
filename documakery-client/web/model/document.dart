part of model;

/**
 * Model object for a folder that contains documents or other child folders.
 * 
 * The folders are hierarchical. Every folder contains a reference to its parent
 * folder or null if it is a root folder.
 */
class DocumentFolder {
  String id;
  String name;
  String parentId;
  List<String> documentIds;
  
  /**
   * Default constructor.
   */
  DocumentFolder();
  
  /**
   * Constructs a [DocumentFolder] from a JSON String.
   */
  DocumentFolder.fromJson(String jsonString) {
    var obj = json.parse(jsonString);
    id = obj['id'];
    name = obj['name'];
    parentId = obj['parentId'];
    documentIds = obj['documentIds'];
  }
  
  /**
   * Converts this object to a JSON map.
   */
  Map toJson() {
    return {
      'id': id,
      'name': name, 
      'parentId': parentId,
      'documentIds': documentIds
    };
  }
  
  String toString() {
    return json.stringify(this);
  }
}

/**
 * Model object for a document.
 */
class Document {
  String id;
  String name;
  List<String> documentBlockIds;
  
  /**
   * Default constructor.
   */
  Document();
  
  /**
   * Constructs a [Document] from a JSON String.
   */
  Document.fromJson(String jsonString) {
    var obj = json.parse(jsonString);
    id = obj['id'];
    name = obj['name'];
    documentBlockIds = obj['documentBlockIds'];
  }
  
  /**
   * Converts this object to a JSON map.
   */
  Map toJson() {
    return {
      'id': id,
      'name': name, 
      'documentBlockIds': documentBlockIds
    };
  }
  
  String toString() {
    return json.stringify(this);
  }
}

/**
 * Model object for a block of [Question]s.
 */
class QuestionBlock {
  String id;
  String title;
  String introduction;
  List<String> questions;
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
}



