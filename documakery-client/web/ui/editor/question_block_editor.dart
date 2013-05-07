library question_block_editor;

import 'dart:html';
import 'dart:async';
import 'package:meta/meta.dart';
import 'package:web_ui/web_ui.dart';

import '../../model/model.dart';
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
    // Add drag and drop listeners.
    // Defer until the end of the event loop so that web components are loaded first.
    Timer.run(() {
      List<Element> questionEditorElements = queryAll('[is="text-question-editor"]');
      for (Element editorElement in questionEditorElements) {
        TextQuestionEditor editor = editorElement.xtag;
        
        // Set relative positioning so that child elements with absolute 
        // positioning will be positioned relative to this element.
        editorElement.style.position = 'relative';
        
        // Listen for drag start
        editorElement.onDragStart.listen(_onDragStart);
        editorElement.onDragEnd.listen(_onDragEnd);
        
        DivElement dropzoneOverlay = 
            _createDropzoneOverlay('${editor.letter}-dropzone-overlay');
        
        // Add overlay as the first child to the editor
        editorElement.children.insert(0, dropzoneOverlay);
            
        // The overlay must listen to all other drag events
        dropzoneOverlay.onDragEnter.listen(_onDragEnter);
        dropzoneOverlay.onDragOver.listen(_onDragOver);
        dropzoneOverlay.onDragLeave.listen(_onDragLeave);
        dropzoneOverlay.onDrop.listen(_onDrop);
      }
    });
  }
  
  /**
   * Creates a dropzone overlay div. The [id] attribute specifies the id that 
   * the dropzone overlay div will receive.
   */
  DivElement _createDropzoneOverlay(String id) {
    return new Element.tag('div')
      ..id = id
      ..classes.add('dropzone-overlay');
  }
  
  void _onDragStart(MouseEvent event) {
    event.stopPropagation();
    
    Element dragTarget = event.target;
    
    // Activate the dropzone overlay
    queryAll('.dropzone-overlay').forEach((Element element) {
      element.style.display = 'block';
    }); 
    
    dragTarget.classes.add('question-block-editor_drag-moving');
    _dragSourceElement = dragTarget;
    event.dataTransfer.effectAllowed = 'move';
  }

  void _onDragEnd(MouseEvent event) {
    event.stopPropagation();
    
    Element dragTarget = event.target;
    dragTarget.classes.remove('question-block-editor_drag-moving');
    List<Element> questionEditors = queryAll('[is="text-question-editor"]');
    for (Element questionEditor in questionEditors) {
      questionEditor.classes.remove('question-block-editor_drag-over');
    }
    
    // Deactivate the dropzone overlay.
    queryAll('.dropzone-overlay').forEach((element) => element.style.display = 'none'); 
  }

  void _onDragEnter(MouseEvent event) {
    event.stopPropagation();
    
    Element dropTarget = event.target;
    dropTarget.parent.classes.add('question-block-editor_drag-over');
  }

  void _onDragOver(MouseEvent event) {
    event.stopPropagation();
    
    // This is necessary to allow us to drop.
    event.preventDefault();
    event.dataTransfer.dropEffect = 'move';
  }

  void _onDragLeave(MouseEvent event) {
    event.stopPropagation();
    
    Element dropTarget = event.target;
    
    dropTarget.parent.classes.remove('question-block-editor_drag-over');
  }

  void _onDrop(MouseEvent event) {
    // Stop the browser from redirecting.
    event.stopPropagation();

    // Don't do anything if dropping onto the same place we're dragging.
    Element dropTarget = event.target;
    if (_dragSourceElement != dropTarget.parent) {
      // Swap the elements.
      _swap(_dragSourceElement, dropTarget.parent);
    }
  }
  
  /**
   * Swaps [e1] and [e2].
   */
  void _swap(Element e1, Element e2) {
    int index1 = _getElementIndexInParent(e1);
    int index2 = _getElementIndexInParent(e2);
    
    Element parent1 = e1.parent;
    Element parent2 = e2.parent;
    
    e1.remove();
    parent2.children.insert(index2, e1);
    
    e2.remove();
    parent1.children.insert(index1, e2);
  }
  
  /**
   * Calculates the index of the Element [e1] in its parent.
   */
  int _getElementIndexInParent(Element e1) {
    int index = 0;
    var previous = e1.previousElementSibling;
    while (previous != null) {
      index++;
      previous = previous.previousElementSibling;
    }
    return index;
  }
}

