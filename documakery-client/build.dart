import 'dart:io';
import 'package:web_ui/component_build.dart';

void main() {
  var args = new List.from(new Options().arguments);
  args.addAll(['--']);
  
  build(args, ['web/documakery.html']);
}
