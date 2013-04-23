library all_tests;

import 'package:unittest/unittest.dart';
import 'package:unittest/interactive_html_config.dart';

import 'model/document_folder_tests.dart' as documentFolder;
import 'model/document_tests.dart' as document;


main() {
  useInteractiveHtmlConfiguration();
  documentFolder.main();
  document.main();
}

