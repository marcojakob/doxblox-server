library doxblox.text_answer_question_editor;

import 'package:polymer/polymer.dart';

import '../../model/model.dart';

/**
 * Editor for [TextQuestion]s.
 */
@CustomTag('doxblox-text-question-editor')
class TextQuestionEditorElement extends PolymerElement {
  
  @published TextQuestion question;
  
  @published String letter;
  
  bool get applyAuthorStyles => true;
  
  TextQuestionEditorElement.created() : super.created();
}
