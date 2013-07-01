part of doxblox.model;

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