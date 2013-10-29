library doxblox.text_answer_question_editor;

import 'package:polymer/polymer.dart';

import '../../model/model.dart';

/**
 * Editor for [TextQuestion]s.
 */
@CustomTag('doxblox-text-question-editor')
class TextQuestionEditorElement extends PolymerElement {
  
  TextQuestion question;
  
  @observable
  String letter;
  
  bool get applyAuthorStyles => true;
  
  TextQuestionEditorElement.created() : super.created();
}
