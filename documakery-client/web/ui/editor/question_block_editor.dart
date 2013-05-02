library question_block_editor;

import 'dart:html';
import 'package:meta/meta.dart';
import 'package:web_ui/web_ui.dart';

import '../../model/model.dart';

/**
 * Editor for a [QuestionBlock].
 */
class QuestionBlockEditor extends WebComponent {
  
  @observable
  QuestionBlock questionBlock;
}
