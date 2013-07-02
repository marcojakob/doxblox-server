library all_tests;

import 'package:unittest/unittest.dart';
import 'package:unittest/html_enhanced_config.dart';

import 'model/model_tests.dart' as modelTests;
import 'ui/ui_tests.dart' as uiTests;

main() {
  useHtmlEnhancedConfiguration();
  modelTests.main();
  uiTests.main();
}