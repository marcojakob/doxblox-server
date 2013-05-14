library question_block_editor;

import 'dart:html';
import 'dart:async';
import 'package:meta/meta.dart';
import 'package:web_ui/web_ui.dart';

import '../../model/model.dart';
import '../util/drag_and_drop.dart';
import 'text_question_editor.dart';

/**
 * Editor for a [QuestionBlock].
 */
class QuestionBlockEditor extends WebComponent {
  
  static final List<String> letters = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 
                                       'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 
                                       's', 't', 'u', 'v', 'w', 'x', 'y', 'z'];
  
  @observable
  QuestionBlock questionBlock;
  
  Element _dragSourceElement;
  
  /**
   * Invoked this component is added to the DOM.
   */
  inserted() {
    // Create observer to watch for [questionBlock] changes.
    observe(() => questionBlock, (ChangeNotification e) {
      // Defer until the end of the event loop so that web components are loaded first.
      Timer.run(() {
        _installDragAndDrop();
      });
    });
    
    // Defer until the end of the event loop so that web components are loaded first.
    Timer.run(() {
      _installDragAndDrop();
    });
  }
  
  void _installDragAndDrop() {
    List<Element> questionEditorElements = queryAll('[is="text-question-editor"]');
    DragAndDrop dnd = new DragAndDrop(questionEditorElements, questionEditorElements);
    dnd.onDrop.listen(DragAndDrop.move);
//    dnd.onDrop.listen(DragAndDrop.swap);
  }
}

