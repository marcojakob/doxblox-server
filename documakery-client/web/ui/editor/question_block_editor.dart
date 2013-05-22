library question_block_editor;

import 'dart:html';
import 'dart:async';
import 'package:meta/meta.dart';
import 'package:web_ui/web_ui.dart';

import '../../model/model.dart';
import '../util/drag_and_drop.dart';
import 'text_question_editor.dart';

import 'package:logging/logging.dart';

final _logger = new Logger("question_block_editor");

/**
 * Editor for a [QuestionBlock].
 */
class QuestionBlockEditor extends WebComponent {
  
  static final List<String> letters = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 
                                       'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 
                                       's', 't', 'u', 'v', 'w', 'x', 'y', 'z'];
  
  @observable
  QuestionBlock questionBlock;
  
  /**
   * Invoked when this component is added to the DOM.
   */
  inserted() {
    // Create observer to watch for [questionBlock] changes.
    observe(() => questionBlock, (_) => _asyncInstallDragAndDrop());
    
    // First installation.
    _asyncInstallDragAndDrop();
  }
  
  
  /**
   * Install Drag and Drop. Defer until the end of the event loop so that web 
   * components are loaded first.
   */
  void _asyncInstallDragAndDrop() {
    Timer.run(() {
      _installDragAndDrop();
    });
  }
  
  /**
   * Install Drag and Drop.
   */
  void _installDragAndDrop() {
    List<Element> questionEditorElements = host.queryAll('[is="text-question-editor"]');
    
    DragAndDropSortable dnd = new DragAndDropSortable(questionEditorElements, 
        questionEditorElements);
    
    dnd.onSortableCompleteController.listen((DragAndDropResult result) {
      int originalIndex = result.originalIndex;
      int newIndex = result.newIndex;
      // Fix the indexes if the first dom child is <template>.
      if (result.dragElement.parent.children.first.tagName.toLowerCase() == 'template') {
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
    List<Element> questionEditorElements = host.queryAll('[is="text-question-editor"]');
    
    for (int i = 0; i < questionEditorElements.length; i++) {
      (questionEditorElements[i].xtag as TextQuestionEditor).letter = letters[i];
    }
  }
}

