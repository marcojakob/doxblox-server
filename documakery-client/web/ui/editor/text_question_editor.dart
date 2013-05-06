library text_answer_question_editor;

import 'dart:html';
import 'package:meta/meta.dart';
import 'package:web_ui/web_ui.dart';

import '../../model/model.dart';

/**
 * Editor for [TextQuestion]s.
 */
class TextQuestionEditor extends WebComponent {
  
  TextQuestion question;
  String letter;
  
  /**
   * Invoked when component is added to the DOM.
   */
  @override
  void inserted() {
  }
}
