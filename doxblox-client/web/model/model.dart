library doxblox.model;

import 'dart:convert';
import 'package:polymer/polymer.dart';
import 'package:meta/meta.dart';
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
   * Converts this object to a JSON map. 
   * 
   * Note: This method is automatically called by json.stringify().
   */
  Map toJson();
}