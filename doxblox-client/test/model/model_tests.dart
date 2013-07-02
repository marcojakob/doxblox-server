library model_tests;

import 'dart:json' as json;
import 'package:unittest/unittest.dart';
import '../../web/model/model.dart';

part 'document/document_tests.dart';
part 'document/document_folder_tests.dart';
part 'document/question/question_block_tests.dart';

main() {
  documentTests();
  documentFolderTests();
  questionBlockTests();
}