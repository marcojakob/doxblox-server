part of mock_data;

Map<String, Document> mockDocuments() {
  return {
      '1' : new Document()
      ..id='1' 
      ..name='Document 1' 
      ..documentBlockIds=['1', '2'],
      
      '2' : new Document()
      ..id='2' 
      ..name='Document 2' 
      ..documentBlockIds=['3', '4', '5'],
      
      '3' : new Document()
      ..id='3' 
      ..name='Document 3' 
      ..documentBlockIds=['1', '2', '4', '5'],
  };
}

