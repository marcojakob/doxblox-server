library mock_data;

import '../../model/model.dart';
import '../data.dart';

part 'mock_document_folders.dart';
part 'mock_documents.dart';

/**
 * Mock implmenentation of [DataAccess].
 */
class MockDataAccess implements DataAccess {
  Map<String, DocumentFolder> _documentFolders;
  Map<String, Document> _documents;
  
  MockDataAccess() {
    _documentFolders = mockDocumentFolders();
    _documents = mockDocuments();
  }
  
  List<DocumentFolder> getDocumentFolders() {
    return _documentFolders.values.toList();
  }
  
  DocumentFolder getDocumentFolderById(String id) {
    return _documentFolders[id];
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
}