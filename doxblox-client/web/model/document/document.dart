part of doxblox.model;

/**
 * Model object for a document.
 */
class Document implements Persistable {
  String id;
  String name;
  List<String> documentBlockIds;
  
  /**
   * Default constructor.
   */
  Document();
  
  /**
   * Constructs a [Document] from the [jsonMap].
   */
  Document.fromJson(Map jsonMap) {
    id = jsonMap['id'];
    name = jsonMap['name'];
    documentBlockIds = jsonMap['documentBlockIds'];
  }
  
  @override
  static FromJsonFactory get fromJsonFactory {
    return (jsonString) => new Document.fromJson(jsonString);
  }
  
  @override
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


