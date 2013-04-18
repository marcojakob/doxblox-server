library document_tests;

import 'dart:json' as json;

import 'package:unittest/unittest.dart';
import '../../web/model/models.dart';

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

main() {
  
group('DocumentFolder Tests:', () {

test('constructorFromJson_AllAttributesSet_ReturnsFolder', () {
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
});

test('constructorFromJson_EmptyJsonString_ReturnsFolderWithNullValues', () {
  // when
  DocumentFolder folder = new DocumentFolder.fromJson('{}');
  
  // then
  expect(folder.id, isNull);
  expect(folder.name, isNull);
  expect(folder.parentId, isNull);
  expect(folder.documentIds, isNull);
});

test('toJsonViaStringify_AllAttributesSet_ReturnsJsonString', () {
  // given
  DocumentFolder folder = new DocumentFolder()
      ..id = '1111'
      ..name = 'class 2d'
      ..parentId = '9999'
      ..documentIds = ['3333', '4444'];
  
  // when
  String jsonString = json.stringify(folder);
  
  // then
  DocumentFolder reconvertedFolder = new DocumentFolder.fromJson(jsonString);
  expect(folder.id, equals('1111'));
  expect(folder.name, equals('class 2d'));
  expect(folder.parentId, equals('9999'));
  expect(folder.documentIds, isList);
  expect(folder.documentIds, equals(["3333", "4444"]));
});
});
}

