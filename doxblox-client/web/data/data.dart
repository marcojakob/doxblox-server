library data;

import '../model/model.dart';

DataAccess _dataAccess;

/// The global [DataAccess] object.
DataAccess get dataAccess => _dataAccess;

/// Initializes the global [DataAccess] object. Should only be called once!
void init(DataAccess dataAccess) {
  _dataAccess = dataAccess;
}

abstract class DataAccess {
  // DocumentFolder
  DocumentFolder getDocumentFolderById(String documentFolderId);
  List<DocumentFolder> getDocumentFolders();
  
  // Document
  Document getDocumentById(String documentId);
  List<Document> getDocuments();
  List<Document> getDocumentsByIds(List<String> documentIds);
  
  // DocumentBlock
  DocumentBlock getDocumentBlockById(String documentBlockId);
  List<DocumentBlock> getDocumentBlocks();
  List<DocumentBlock> getDocumentBlocksByIds(List<String> documentBlockIds);
}
