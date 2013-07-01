part of doxblox.mock_data;

class MockDocumentFolderDao extends MockDao implements DocumentFolderDao {
  
  MockDocumentFolderDao() {
    
    data[newId()] = new DocumentFolder()
    ..id = lastId()
    ..name = 'Folder 1' 
    ..parentId = null 
    ..documentIds = ['1'];
    
    data[newId()] = new DocumentFolder()
    ..id = lastId()
    ..name = 'Folder 1-1' 
    ..parentId = '1'
    ..documentIds = ['2', '3'];
    
    data[newId()] = new DocumentFolder()
    ..id = lastId()
    ..name = 'Folder 1-1-1' 
    ..parentId = '2'
    ..documentIds = [];
    
    data[newId()] = new DocumentFolder()
    ..id = lastId()
    ..name = 'Folder 1-1-2' 
    ..parentId = '2'
    ..documentIds = [];
    
    data[newId()] = new DocumentFolder()
    ..id = lastId()
    ..name = 'Folder 1-2' 
    ..parentId = '1'
    ..documentIds = [];
    
    data[newId()] = new DocumentFolder()
    ..id = lastId()
    ..name = 'Folder 1-3' 
    ..parentId = '1'
    ..documentIds = [];
  }
}

