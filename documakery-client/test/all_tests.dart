library all_tests;

import 'package:unittest/unittest.dart';
import 'package:unittest/html_enhanced_config.dart';

import 'model/document_folder_tests.dart' as documentFolder;
import 'model/document_tests.dart' as document;
import 'ui/navigation/tree_node_tests.dart' as treeNode;


main() {
  useHtmlEnhancedConfiguration();
  documentFolder.main();
  document.main();
  treeNode.main();
}

