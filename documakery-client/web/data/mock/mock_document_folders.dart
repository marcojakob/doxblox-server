part of mock_data;

Map<String, DocumentFolder> mockDocumentFolders() {
  return {
  '1' : new DocumentFolder()
  ..id = '1' 
  ..name = 'Folder 1' 
  ..parentId = null 
  ..documentIds = [],
  
  '1-1' : new DocumentFolder()
  ..id = '1-1' 
  ..name = 'Folder 1-1' 
  ..parentId = '1'
  ..documentIds = ['1'],
  
  '1-1-1' : new DocumentFolder()
  ..id = '1-1-1' 
  ..name = 'Folder 1-1-1' 
  ..parentId = '1-1'
  ..documentIds = ['1', '2'],
  
  '1-1-2' : new DocumentFolder()
  ..id = '1-1-2' 
  ..name = 'Folder 1-1-2' 
  ..parentId = '1-1'
  ..documentIds = ['1', '2', '3'],
  
  '1-2' : new DocumentFolder()
  ..id = '1-2'
  ..name = 'Folder 1-2' 
  ..parentId = '1'
  ..documentIds = ['2', '3'],
  
  '1-3' : new DocumentFolder()
  ..id = '1-3'
  ..name = 'Folder 1-3' 
  ..parentId = '1'
  ..documentIds = []
  };
}

