library data_access;

import '../model/models.dart';

part 'mock/mock_data_access.dart';
part 'mock/mock_document_folders.dart';

abstract class DataAccess {
  List<DocumentFolder> get documentFolders;
}
