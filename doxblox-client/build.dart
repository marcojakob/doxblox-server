import 'dart:io';
import 'package:web_ui/component_build.dart';

void main() {
  var args = new List.from(new Options().arguments);
  build(args, ['web/doxblox.html']);
}
