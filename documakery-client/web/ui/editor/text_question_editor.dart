library text_answer_question_editor;

import 'dart:html';
import 'package:meta/meta.dart';
import 'package:web_ui/web_ui.dart';

import '../../model/model.dart';

/**
 * Editor for [TextQuestion]s.
 */
class TextQuestionEditor extends WebComponent {
  
  static final List<String> letters = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 
                                'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 
                                's', 't', 'u', 'v', 'w', 'x', 'y', 'z'];
  
  TextQuestion question;
  int index;
  
  /**
   * Lifecycle method invoked whenever a component is added to the DOM.
   */
  @override
  void inserted() {
  }
  
  String indexLetter() {
    return letters[index];
  }
}
