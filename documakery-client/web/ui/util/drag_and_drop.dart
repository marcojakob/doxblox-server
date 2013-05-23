/**
 * Helper library for drag and drop.
 */
library drag_and_drop;

import 'dart:html';
import 'dart:async';
import 'package:meta/meta.dart';
import 'package:js/js.dart' as js;
import 'package:logging/logging.dart';

final _logger = new Logger("drag_and_drop");

/**
 * Helper class for drag and drop. There are draggable elements that can be 
 * dropped inside dropzone elements. 
 * 
 * ## CSS Classes ##
 * * .dnd-moving: The draggable element beeing dragged.
 * * .dnd-over: The dropzone element over which a draggable element is.
 * * .dnd-involved: All drag and drop elements involved during a drag. This
 *   is helpful to disable hovering during a drag by using the css selector 
 *   :not(.dnd-involved):hover
 * 
 * ## Events ##
 * Subclasses must implement the following drag-and-drop events:
 * * onDragStart
 * * onDragEnd
 * * onDragEnter
 * * onDragOver
 * * onDragLeave
 * * onDrop
 */
abstract class DragAndDrop {
  const String CSS_CLASS_MOVING = 'dnd-moving';
  const String CSS_CLASS_OVER = 'dnd-over';
  const String CSS_CLASS_INVOLVED = 'dnd-involved';
  
  /// Currently dragged element.
  Element _currentDragElement;
 
  /// Keeps track of all draggable and dropzone elements.
  Set<Element> _dndElements = new Set<Element>();
  
  /**
   * Installs listeners on the [draggableElement].
   */
  void installDraggable(Element draggableElement) {
    _dndElements.add(draggableElement);
    
    _enableIE9drag(draggableElement);
    
    // Drag Start.
    draggableElement.onDragStart.listen((MouseEvent event) {
      // Add CSS classes
      _addCssClass(draggableElement, CSS_CLASS_MOVING);
      _dndElements.forEach((Element e) => _addCssClass(e, CSS_CLASS_INVOLVED));
      
      _currentDragElement = draggableElement;
      
      event.dataTransfer.effectAllowed = getEffectAllowedFor(draggableElement);
      getDataFor(draggableElement).forEach((String type, String data) {
        event.dataTransfer.setData(type, data);
      });
      
      onDragStart(new DragAndDropEvent(draggableElement));
    });
    
    // Drag End.
    draggableElement.onDragEnd.listen((MouseEvent event) {
      // Remove CSS classes.
      _removeCssClass(draggableElement, CSS_CLASS_MOVING);
      _dndElements.forEach((Element e) => _removeCssClass(e, CSS_CLASS_INVOLVED));
      
      _currentDragElement = null;
      
      onDragEnd(new DragAndDropEvent(draggableElement));
    });
  }
  
  /**
   * Workaround to enable drag-and-drop elements other than links and images in
   * Internet Explorer 9.
   */
  void _enableIE9drag(Element element) {
    if (element.draggable == null) {
      // HTML5 draggable support is not available --> try to use workaround.
      _logger.finest('Draggable is null, installing dragDrop() workaround');
      
      element.onSelectStart.listen((MouseEvent event) {
        // Prevent selection of text.
        event.preventDefault();
        
        js.context.callDragDrop(element);
      });
    }
  }
  
  
  /**
   * Installs listeners on the [dropzoneElement].
   */
  void installDropzone(Element dropzoneElement) {
    _dndElements.add(dropzoneElement);
    
    // Keep track of elements where dragEnter or dragLeave has been fired on.
    // This is necessary as a dragEnter or dragLeave event is not only fired
    // on the [dropzoneElement] but also on its children. Now, whenever the 
    // [dragOverElements] is empty we know the dragEnter or dragLeave event
    // was fired on the real [dropzoneElement].
    Set<Element> dragOverElements = new Set<Element>();
    
    // Drag Enter.
    dropzoneElement.onDragEnter.listen((MouseEvent event) {
      // Do nothing if no element of this dnd is dragged.
      if (_currentDragElement == null) return;
      
      // This is necessary for IE.
      event.preventDefault();
      
      // Only handle on dropzone element and not on any of its children.
      if (dragOverElements.length == 0) {
        _addCssClass(dropzoneElement, CSS_CLASS_OVER);
             
        onDragEnter(new DragAndDropEvent(_currentDragElement, dropzoneElement));
      }
      dragOverElements.add(event.target);
    });
    
    // Drag Over.
    dropzoneElement.onDragOver.listen((MouseEvent event) {
      // Do nothing if no element of this dnd is dragged.
      if (_currentDragElement == null) return;
      
      // This is necessary to allow us to drop.
      event.preventDefault();
      
      event.dataTransfer.dropEffect = 
          getDropEffectFor(_currentDragElement, dropzoneElement);
      
      onDragOver(new DragAndDropEvent(_currentDragElement, dropzoneElement));
    });
    
    // Drag Leave.
    dropzoneElement.onDragLeave.listen((MouseEvent event) {
      // Do nothing if no element of this dnd is dragged.
      if (_currentDragElement == null) return;
      
      // Firefox fires too many onDragLeave events. This condition fixes it. 
      if (event.target != event.relatedTarget) {
        dragOverElements.remove(event.target);
      }
      
      // Only handle on dropzone element and not on any of its children.
      if (dragOverElements.length == 0) {
        _removeCssClass(dropzoneElement, CSS_CLASS_OVER);
        
        onDragLeave(new DragAndDropEvent(_currentDragElement, dropzoneElement));
      }
    });
    
    // Drop.
    dropzoneElement.onDrop.listen((MouseEvent event) {
      // Do nothing if no element of this dnd is dragged.
      if (_currentDragElement == null) return;
      
      // Stop the browser from redirecting.
      event.preventDefault();
      
      _removeCssClass(dropzoneElement, CSS_CLASS_OVER);
      dragOverElements.clear();
      
      onDrop(new DragAndDropEvent(_currentDragElement, dropzoneElement));
    });
  }
  
  void onDragStart(DragAndDropEvent event);
  void onDragEnd(DragAndDropEvent event);
  void onDragEnter(DragAndDropEvent event);
  void onDragOver(DragAndDropEvent event);
  void onDragLeave(DragAndDropEvent event);
  void onDrop(DragAndDropEvent event);
  
  /**
   * Restricts what 'type of drag' the user can perform on the [dragElement]. 
   * It is used in the drag-and-drop processing model to initialize the 
   * dropEffect during the dragenter and dragover events. The property can be 
   * set to the following values: none, copy, copyLink, copyMove, link, 
   * linkMove, move, all, and uninitialized.
   */
  String getEffectAllowedFor(Element dragElement);
  
  /**
   * Controls the feedback that the user is given during the dragenter and 
   * dragover events. When the user hovers over a target element, the browser's
   * cursor will indicate what type of operation is going to take place (e.g. a 
   * copy, a move, etc.). The effect can take on one of the following values: 
   * none, copy, link, move.
   */
  String getDropEffectFor(Element dragElement, Element dropzoneElement);
  
  /**
   * Creates data for [:dataTransfer.setData:] on the event. The result is a map
   * with the type as key and the data as value.
   */
  Map<String, String> getDataFor(Element dragElement);
  
  void _addCssClass(Element element, String cssClass) {
    element.classes.add(cssClass);
    
    // Workaround for scoped css inside web components.
    if (element.attributes.containsKey('is')) {
      String scopedCssPrefix = '${element.attributes['is']}_';
      element.classes.add(scopedCssPrefix + cssClass);
    }
  }
  
  void _removeCssClass(Element element, String cssClass) {
    element.classes.remove(cssClass);
    
    // Workaround for scoped css inside web components.
    if (element.attributes.containsKey('is')) {
      String scopedCssPrefix = '${element.attributes['is']}_';
      element.classes.remove(scopedCssPrefix + cssClass);
    }
  }
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

/**
 * Helper class for drag-and-drop streams to report events. It contains a 
 * [dragElement] and a [dropzoneElement]. 
 * 
 * **Note:** The [dropzoneElement] is null for onDragStart and onDragEnd events.
 */
class DragAndDropEvent {
  /// The element beeing dragged.
  Element dragElement;
  
  /// The dropzone element. May be null.
  Element dropzoneElement;
  
  /**
   * Creates a [DragAndDropEvent]. The [dragElement] must always be provided. 
   * The [dropzoneElement] is optional.
   */
  DragAndDropEvent(this.dragElement, [this.dropzoneElement=null]);
}

/**
 * Result used to carry information about a completed drag-and-drop operation.
 * The [dragElement] was moved and has the [newIndex] inside its new parent. 
 * Also provides info about the [originalParent] and [originalIndex] in 
 * the [originalParent].
 */
class DragAndDropResult {
  Element dragElement;
  int newIndex;
  Element originalParent;
  int originalIndex;
  
  DragAndDropResult(this.dragElement, this.newIndex, this.originalParent,
      this.originalIndex);
}

/**
 * Drag and Drop for reordering elements.
 */
class DragAndDropSortable extends DragAndDrop {
  
  /// Index of the draggable element in its parent before dragging.
  int _originalIndex;
  
  /// Parent of the draggable element before dragging.
  Element _originalParent;
  
  /// Index of the draggable element in its parent after dragging.
  int _newIndex;
  
  /// Indicates (for the [onDragEnd] event) if the drag element has been dropped. 
  /// If false, no real drop on a dropzone element has happened, i.e. the user 
  /// aborted the dnd.
  bool _dropped;

  /// Callback function that is called when the drag is ended by a drop.
  /// If the user aborted the drag, the function is not called.
  StreamController<DragAndDropResult> _onSortableCompleteController = 
      new StreamController<DragAndDropResult>();
  
  /**
   * Cunstructs and initializes sortable drag and drop for the specified 
   * [draggableElements] and [dropzoneElements]. 
   */
  DragAndDropSortable(List<Element> draggableElements, 
      List<Element> dropzoneElements) {
    // Install draggable elements.
    for (Element draggableElement in draggableElements) {
      installDraggable(draggableElement);
    }
    
    // Install dropzone elements.
    for (Element dropzoneElement in dropzoneElements) {
      installDropzone(dropzoneElement);
    }
  }
  
  /**
   * Returns the stream of completed sortable drag-and-drop events.
   * If the user aborted the drag, no event is fired.
   */
  Stream<DragAndDropResult> get onSortableCompleteController {
    if (_onSortableCompleteController == null) {
      _onSortableCompleteController = new StreamController<DragAndDropResult>();
    }
    return _onSortableCompleteController.stream;
  }
  
  @override
  void onDragStart(DragAndDropEvent event) {
    _originalIndex = _getElementIndexInParent(event.dragElement);
    _originalParent = event.dragElement.parent;
    _newIndex = _originalIndex;
    _dropped = false;
  }
  
  @override
  void onDragEnd(DragAndDropEvent event) {
    if (!_dropped) {
      // Not dropped: reset to state before dragging.
      _moveTo(event.dragElement, _originalParent, _originalIndex);
    }
  }
  
  @override
  void onDragEnter(DragAndDropEvent event) {
    if (event.dragElement == event.dropzoneElement) {
      return;
    }
    
    Element newParent = event.dropzoneElement.parent;
    _newIndex = _getElementIndexInParent(event.dropzoneElement);
    
    _moveTo(event.dragElement, newParent, _newIndex);
  }
  
  @override
  void onDragOver(DragAndDropEvent event) {
  }
  
  @override
  void onDragLeave(DragAndDropEvent event) {
  }
  
  @override
  void onDrop(DragAndDropEvent event) {
    _dropped = true;
    
    if (_onSortableCompleteController != null 
        && _onSortableCompleteController.hasListener
        && !_onSortableCompleteController.isClosed
        && !_onSortableCompleteController.isPaused) {
      
      _onSortableCompleteController.add(new DragAndDropResult(event.dragElement, 
          _newIndex, _originalParent, _originalIndex));
    }
  }
  
  @override
  String getEffectAllowedFor(Element dragElement) {
    // Only allow move effect.
    return 'move';
  }
  
  @override
  String getDropEffectFor(Element dragElement, Element dropzoneElement) {
    return 'move';
  }
  
  @override
  Map<String, String> getDataFor(Element dragElement) {
    return {
      // 'text/html' doesn't seem to work in ie.
      'text': dragElement.innerHtml
    };
  }

  /**
   * Moves the [element] to the specified [index] in the [parent] element.
   * 
   * Note: The maximum [index] is the number of children of the parent. If the 
   * move happens inside the same parent, the maximum [index] is children minus 
   * 1. This is because the [element] itself is removed from its parent before 
   * it is added again and thus there is one less child when it is inserted.
   */
  void _moveTo(Element element, Element parent, int index) {
    element.remove();
    parent.children.insert(index, element); 
  }
}




