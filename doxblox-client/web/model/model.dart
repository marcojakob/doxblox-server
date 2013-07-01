library doxblox.model;

import 'dart:json' as json;
import 'package:meta/meta.dart';
import 'package:doxblox/string_utils.dart';
import 'package:web_ui/observe.dart';

import '../data/data.dart';

part 'document_folder.dart';
part 'document.dart';
part 'document_block.dart';

part 'question.dart';

/**
 * Function used to create subclasses of [Persistable] from the [jsonString].
 */
typedef Persistable FromJsonFactory(String jsonString);

/**
 * Persistable model object.
 */
abstract class Persistable {
  
  /**
   * Returns the id.
   */
  String get id;
  
  /**
   * Sets the id.
   */
  set id(String id);
  
  /**
   * Returns the [FromJsonFactory] that is used to create subclasses of 
   * [Persistable] from a JSON String.
   */
  FromJsonFactory get fromJsonFactory;
  
  /**
   * Converts this object to a JSON map. 
   * 
   * Note: This method is automatically called by json.stringify().
   */
  Map toJson();
}