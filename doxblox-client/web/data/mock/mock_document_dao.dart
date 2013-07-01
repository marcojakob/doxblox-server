part of doxblox.mock_data;

class MockDocumentDao extends MockDao implements DocumentDao {
  
  MockDocumentDao() {
    data[newId()] = new Document()
    ..id = lastId()
    ..name = 'Document 1' 
    ..documentBlockIds = ['1', '2', '5', '6', '7', '8', '9'];

    data[newId()] = new Document()
    ..id = lastId()
    ..name = 'Document 2' 
    ..documentBlockIds = ['3', '4', '5', '9'];
      
    data[newId()] = new Document()
    ..id = lastId()
    ..name = 'Document 3' 
    ..documentBlockIds = ['1', '2', '4', '5'];
  }
}

