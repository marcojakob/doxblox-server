library doxblox.question_block_editor;

import 'package:polymer/polymer.dart';
import 'dart:html';
import 'dart:async';
import 'package:html5_dnd/html5_dnd.dart';

import '../../model/model.dart';
import 'text_question_editor.dart';

import 'package:logging/logging.dart';

final _logger = new Logger("doxblox.question_block_editor");

/**
 * Editor for a [QuestionBlock].
 */
@CustomTag('doxblox-question-block-editor')
class QuestionBlockEditorElement extends PolymerElement {
  
  static final List<String> letters = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 
                                       'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 
                                       's', 't', 'u', 'v', 'w', 'x', 'y', 'z'];
  
  @observable
  QuestionBlock questionBlock;
  
  bool get applyAuthorStyles => true;
  
  QuestionBlockEditorElement.created() : super.created();
  
  /**
   * Invoked when this component is added to the DOM.
   */
  void enteredView() {
    super.enteredView();
    
    // First installation. Defer until the end of the event loop so that web 
    // components are loaded first.
    new Future(_installDragAndDrop);
  }
  
  /**
   * Is automatically called when the [questionBlock] attribute changed.
   */
  void questionBlockChanged(QuestionBlock oldQuestionBlock) {
    // Defer until the end of the event loop so that web components are loaded 
    // first.
    new Future(_installDragAndDrop);
  }
  
  /**
   * Install drag and drop to enable reordering of questions.
   */
  void _installDragAndDrop() {
    List<Element> questionEditorElements = 
        shadowRoot.querySelectorAll('text-question-editor');
    
    SortableGroup dndGroup = new SortableGroup()
    ..installAll(questionEditorElements)
    ..onSortUpdate.listen((SortableEvent event) {
      int originalIndex = event.originalPosition.index;
      int newIndex = event.newPosition.index;
      // Fix the indexes if the first dom child is <template>.
      if (event.draggable.parent.children.first.tagName.toLowerCase() == 'template') {
        originalIndex--;
        newIndex--;
      }
      _logger.fine('drag-and-drop completed with originalIndex=$originalIndex, newIndex=$newIndex');
      
      // Move question inside the questionBlock.
      var draggedQuestion = questionBlock.questions.removeAt(originalIndex);
      questionBlock.questions.insert(newIndex, draggedQuestion);
      
      _refreshLetters();
    });
  }
  
  void _refreshLetters() {
    List<Element> questionEditorElements = 
        shadowRoot.querySelectorAll('text-question-editor');
    
    for (int i = 0; i < questionEditorElements.length; i++) {
      (questionEditorElements[i] as TextQuestionEditorElement).letter = letters[i];
    }
  }
}

