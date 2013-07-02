library doxblox.model;

import 'dart:json' as json;
import 'package:meta/meta.dart';
import 'package:web_ui/observe.dart';
import 'package:logging/logging.dart';

import '../data/data.dart';
import '../util/string_utils.dart';

part 'document/document_folder.dart';
part 'document/document.dart';
part 'document/document_block.dart';

part 'document/question/question.dart';
part 'document/question/question_block.dart';
part 'document/question/text_question.dart';

final _log = new Logger("doxblox.model");

/**
 * Function used to create subclasses of [Persistable] from the [jsonMap].
 */
typedef Persistable FromJsonFactory(Map jsonMap);

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
   * [Persistable] from a JSON Map. This is a trick to get access to the 
   * fromJson constructor without using the mirror API.
   */
  FromJsonFactory get fromJsonFactory;
  
  /**
   * Converts this object to a JSON map. 
   * 
   * Note: This method is automatically called by json.stringify().
   */
  Map toJson();
}