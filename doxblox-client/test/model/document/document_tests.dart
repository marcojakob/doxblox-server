part of model_tests;

const String DOCUMENT_JSON = '''
{
  "id" : "51267b98f5c757f73f98dc1e",
  "name" : "Klasse E1.603",
  "documentBlockIds" : [ 
    "51267930f5c7f925eaae04d1",
    "51267930f5c7f925eaae04d7"
  ]
}
''';

/**
 * Tests for the [Document] model object.
 */
documentTests() {
  group('Document Tests:', () {
  
  test('constructorFromJson_AllAttributesSet_ReturnsFolder', () {
    // when
    Document document = new Document.fromJson(json.parse(DOCUMENT_JSON));
    
    // then
    expect(document.id, equals('51267b98f5c757f73f98dc1e'));
    expect(document.name, equals('Klasse E1.603'));
    expect(document.documentBlockIds, isList);
    expect(document.documentBlockIds, equals([ 
      "51267930f5c7f925eaae04d1",
      "51267930f5c7f925eaae04d7"]));
  });
  
  test('constructorFromJson_EmptyJsonString_ReturnsDocumentWithNullValues', () {
    // when
    Document document = new Document.fromJson(json.parse('{}'));
    
    // then
    expect(document.id, isNull);
    expect(document.name, isNull);
    expect(document.documentBlockIds, isNull);
  });
  
  test('toJsonViaStringify_AllAttributesSet_ReturnsJsonString', () {
    // given
    Document document = new Document()
        ..id = '1111'
        ..name = 'class 2d'
        ..documentBlockIds = ['3333', '4444'];
    
    // when
    String jsonString = json.stringify(document);
    
    // then
    Document reconvertedDoc = new Document.fromJson(json.parse(jsonString));
    expect(reconvertedDoc.id, equals('1111'));
    expect(reconvertedDoc.name, equals('class 2d'));
    expect(reconvertedDoc.documentBlockIds, isList);
    expect(reconvertedDoc.documentBlockIds, equals(["3333", "4444"]));
  });
  
  test('toString', () {
    // given
    Document document = new Document()
    ..id = '1111'
    ..name = 'class 2d'
    ..documentBlockIds = ['3333', '4444'];
    
    // when
    String str = document.toString();
    
    // then
    expect(str, equals('{"id":"1111","name":"class 2d","documentBlockIds":["3333","4444"]}'));
  });
  });
}

