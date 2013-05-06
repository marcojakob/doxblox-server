library question_block_editor;

import 'dart:html';
import 'package:meta/meta.dart';
import 'package:web_ui/web_ui.dart';

import '../../model/model.dart';

/**
 * Editor for a [QuestionBlock].
 */
class QuestionBlockEditor extends WebComponent {
  
  static final List<String> letters = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 
                                       'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 
                                       's', 't', 'u', 'v', 'w', 'x', 'y', 'z'];
  @observable
  QuestionBlock questionBlock;
}
