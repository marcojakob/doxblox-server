part of doxblox.model;

/**
 * Model object for a folder that contains documents or other child folders.
 * 
 * The folders are hierarchical. Every folder contains a reference to its parent
 * folder or null if it is a root folder.
 */
class DocumentFolder implements Persistable {
  String id;
  String name;
  String parentId;
  List<String> documentIds;
  
  /**
   * Default constructor.
   */
  DocumentFolder();
  
  /**
   * Constructs a [DocumentFolder] from the [jsonMap].
   */
  DocumentFolder.fromJson(Map jsonMap) {
    id = jsonMap['id'];
    name = jsonMap['name'];
    parentId = jsonMap['parentId'];
    documentIds = jsonMap['documentIds'];
  }
  
  @override
  Map toJson() {
    return {
      'id': id,
      'name': name, 
      'parentId': parentId,
      'documentIds': documentIds
    };
  }
  
  String toString() {
    return JSON.encode(this);
  }
}