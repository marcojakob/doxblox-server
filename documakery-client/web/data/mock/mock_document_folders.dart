part of data_access;

List<DocumentFolder> mockDocumentFolders() {
  return [
      new DocumentFolder()
      ..id='1' 
      ..name='1' 
      ..parentId=null 
      ..documentIds=['3333', '4444'],
      
      new DocumentFolder()
      ..id='1.1' 
      ..name='1.1' 
      ..parentId='1'
      ..documentIds=['3333', '4444'],
      
      new DocumentFolder()
      ..id='1.1.1' 
      ..name='1.1.1' 
      ..parentId='1.1'
      ..documentIds=['3333', '4444'],
      
      new DocumentFolder()
      ..id='1.1.2' 
      ..name='1.1.2' 
      ..parentId='1.1'
      ..documentIds=['3333', '4444'],
      
      new DocumentFolder()
      ..id='1.2'
      ..name='1.2' 
      ..parentId='1'
      ..documentIds=['3333', '4444'],
      
      new DocumentFolder()
      ..id='1.3'
      ..name='1.3' 
      ..parentId='1'
      ..documentIds=['3333', '4444']
  ];
}

