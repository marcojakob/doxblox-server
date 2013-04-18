part of data_access;

/**
 * Mock implmenentation of [DataAccess].
 */
class MockDataAccess implements DataAccess {
  List<DocumentFolder> documentFolders;
  
  MockDataAccess() {
    documentFolders = mockDocumentFolders();
  }
}