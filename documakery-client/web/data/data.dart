library data;

import '../model/model.dart';

abstract class DataAccess {
  List<DocumentFolder> getDocumentFolders();
  DocumentFolder getDocumentFolderById(String id);
  
  List<Document> getDocuments();
  List<Document> getDocumentsByIds(List<String> documentIds);
}
