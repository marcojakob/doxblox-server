part of models;

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
  DocumentFolder() {
  }
  
  /**
   * Constructs a [DocumentFolder] from a JSON String.
   */
  DocumentFolder.fromJson(String jsonString) {
    var folder = json.parse(jsonString);
    id = folder['id'];
    name = folder['name'];
    parentId = folder['parentId'];
    documentIds = folder['documentIds'];
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
}



