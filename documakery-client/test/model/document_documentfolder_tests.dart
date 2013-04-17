library document_tests;

import 'package:unittest/unittest.dart';
import 'dart:json' as json;
import '../../web/model/document.dart';

const String DOCUMENT_FOLDER_JSON = '''
{
  "id" : "51267b98f5c757f73f98dc1e",
  "name" : "Klasse E1.603",
  "parentId" : "51267b98f5c757f73f98dc1d",
  "documentIds" : [ 
    "51267930f5c7f925eaae04d1",
    "51267930f5c7f925eaae04d7"
  ]
}
''';

constructorFromJson() {
  // when
  DocumentFolder folder = new DocumentFolder.fromJson(DOCUMENT_FOLDER_JSON);
  
  // then
  expect(folder.id, equals('51267b98f5c757f73f98dc1e'));
  expect(folder.name, equals('Klasse E1.603'));
  expect(folder.parentId, equals('51267b98f5c757f73f98dc1d'));
  expect(folder.documentIds, isList);
  expect(folder.documentIds, equals([ 
    "51267930f5c7f925eaae04d1",
    "51267930f5c7f925eaae04d7"]));
}

toJson_ViaJsonStringify() {
  // given
  DocumentFolder folder = new DocumentFolder('1111', 'class 2d', '9999', ['3333', '4444']);
  
  // when
  String jsonString = json.stringify(folder);
  
  // then
  expect(jsonString, equals('{"id":"1111","name":"class 2d","parentId":"9999","documentIds":["3333","4444"]}'));
}

main() {
  group('DocumentFolder tests:', () {
    test('constructorFromJson', constructorFromJson);
    test('toJson_ViaJsonStringify', toJson_ViaJsonStringify);
  });
}

