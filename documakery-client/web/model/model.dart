library model;

import 'dart:json' as json;
import 'package:documakery/string_utils.dart';
import 'package:web_ui/observe.dart';

part 'document.dart';
part 'question.dart';

/**
 * Persistable behaviour used as a mixin.
 */
abstract class Persistable {
  
  void save() {
    print('saving');
  }
  
  void load() {
    print('loading');
  }
  
  /**
   * Converts this object to a JSON map.
   */
  Map toJson();
}