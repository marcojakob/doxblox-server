library mock_data;

import '../../model/model.dart';
import '../data.dart';

part 'mock_document_folders.dart';
part 'mock_documents.dart';
part 'mock_question_blocks.dart';

/**
 * Mock implmenentation of [DataAccess].
 */
class MockDataAccess implements DataAccess {
  Map<String, DocumentFolder> _documentFolders;
  Map<String, Document> _documents;
  Map<String, QuestionBlock> _questionBlocks;
  
  MockDataAccess() {
    _documentFolders = mockDocumentFolders();
    _documents = mockDocuments();
    _questionBlocks = mockQuestionBlocks();
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
  
  // QuestionBlock
  List<QuestionBlock> getQuestionBlocksByIds(List<String> questionBlockIds) {
    List<QuestionBlock> result = new List();
    for (String id in questionBlockIds) {
      result.add(_questionBlocks[id]);
    }
    return result;
  }
}