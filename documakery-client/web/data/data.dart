library data;

import '../model/model.dart';

abstract class DataAccess {
  // DocumentFolder
  DocumentFolder getDocumentFolderById(String id);
  List<DocumentFolder> getDocumentFolders();
  
  // Document
  Document getDocumentById(String id);
  List<Document> getDocuments();
  List<Document> getDocumentsByIds(List<String> documentIds);
  
  // QuestionBlock
  List<QuestionBlock> getQuestionBlocksByIds(List<String> questionBlockIds);
}
