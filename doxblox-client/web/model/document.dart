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
   * Constructs a [Document] from a JSON String.
   */
  Document.fromJson(String jsonString) {
    var obj = json.parse(jsonString);
    id = obj['id'];
    name = obj['name'];
    documentBlockIds = obj['documentBlockIds'];
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


