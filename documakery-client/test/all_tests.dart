library all_tests;

import 'package:unittest/unittest.dart';
import 'package:unittest/interactive_html_config.dart';
import 'model/document_documentfolder_tests.dart' as document;

main() {
  useInteractiveHtmlConfiguration();
  document.main();
}

