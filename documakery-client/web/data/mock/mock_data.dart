library mock_data;

import '../../model/model.dart';
import '../data.dart';

part 'mock_document_folders.dart';
part 'mock_documents.dart';
part 'mock_document_blocks.dart';

/**
 * Mock implmenentation of [DataAccess].
 */
class MockDataAccess implements DataAccess {
  Map<String, DocumentFolder> _documentFolders;
  Map<String, Document> _documents;
  Map<String, DocumentBlock> _documentBlocks;
  
  MockDataAccess() {
    _documentFolders = mockDocumentFolders();
    _documents = mockDocuments();
    _documentBlocks = mockQuestionBlocks();
  }
  
  // DocumentFolder
  DocumentFolder getDocumentFolderById(String id) {
    return _documentFolders[id];
  }
  
  List<DocumentFolder> getDocumentFolders() {
    return _documentFolders.values.toList();
  }
  
  // Document
  Document getDocumentById(String id) {
    return _documents[id];  
  }
  
  List<Document> getDocuments() {
    return _documents.values.toList();
  }
  
  List<Document> getDocumentsByIds(List<String> documentIds) {
    List<Document> result = new List();
    for (String id in documentIds) {
      result.add(_documents[id]);
    }
    return result;
  }
  
  // DocumentBlock
  List<DocumentBlock> getDocumentBlocksByIds(List<String> documentBlockIds) {
    List<DocumentBlock> result = new List();
    for (String id in documentBlockIds) {
      result.add(_documentBlocks[id]);
    }
    return result;
  }
}