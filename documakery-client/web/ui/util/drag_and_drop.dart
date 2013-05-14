/**
 * Helper library for drag and drop.
 */
library drag_and_drop;

import 'dart:html';
import 'dart:async';
import 'package:js/js.dart' as js;
import 'package:event_bus/event_bus.dart' show BroadcastStreamController;

/**
 * Helper class for drag and drop. There are [draggableElements] that can be 
 * dropped inside [dropzoneElements]. 
 * 
 * ## CSS Classes ##
 * * The element beeing dragged gets the CSS class [:dnd-moving:].
 * * The dropzone element over which a dragged element is gets [:dnd-over:].
 * 
 * ## Events ##
 * There is a [Stream] for all six drag-and-drop events:
 * * onDragStart
 * * onDragEnd
 * * onDragEnter
 * * onDragOver
 * * onDragLeave
 * * onDrop
 * 
 * There are some predefined static handler methods that may be used as handler 
 * for the events:
 * * move
 * * swap 
 */
class DragAndDrop {
  const String CSS_CLASS_MOVING = 'dnd-moving';
  const String CSS_CLASS_OVER = 'dnd-over';
  
  // The stream controllers.
  BroadcastStreamController<DragAndDropEvent> _onDragStartController = new BroadcastStreamController<DragAndDropEvent>();
  BroadcastStreamController<DragAndDropEvent> _onDragEndController = new BroadcastStreamController<DragAndDropEvent>();
  BroadcastStreamController<DragAndDropEvent> _onDragEnterController = new BroadcastStreamController<DragAndDropEvent>();
  BroadcastStreamController<DragAndDropEvent> _onDragOverController = new BroadcastStreamController<DragAndDropEvent>();
  BroadcastStreamController<DragAndDropEvent> _onDragLeaveController = new BroadcastStreamController<DragAndDropEvent>();
  BroadcastStreamController<DragAndDropEvent> _onDropController = new BroadcastStreamController<DragAndDropEvent>();
  
  /// The elements that can be dragged.
  List<Element> draggableElements;
  
  /// The elements on which a dragged element can be dropped.
  List<Element> dropzoneElements;
  
  /// Currently dragged element.
  Element _currentDragElement;
  
  /**
   * Cunstructs and initializes drag and drop for the specified 
   * [draggableElements] and [dropzoneElements]. 
   */
  DragAndDrop(this.draggableElements, this.dropzoneElements) {
    
    // Add listeners to draggable elements.
    for (Element draggableElement in draggableElements) {
      _installDraggableListeners(draggableElement);
    }
    
    // Add listeners to dropzone elements.
    for (Element dropzoneElement in dropzoneElements) {
      _installDropzoneListeners(dropzoneElement);
    }
  }

  void _installDraggableListeners(Element draggableElement) {
    // Drag Start.
    draggableElement.onDragStart.listen((MouseEvent event) {
      draggableElement.classes.add(CSS_CLASS_MOVING);
      _currentDragElement = draggableElement;
      
      event.dataTransfer.effectAllowed = 'move';
      event.dataTransfer.setData('text/plain', draggableElement.innerHtml);
      
      _fireEvent(_onDragStartController, draggableElement);
    });
    
    // Drag End.
    draggableElement.onDragEnd.listen((MouseEvent event) {
      draggableElement.classes.remove(CSS_CLASS_MOVING);
      
      _fireEvent(_onDragEndController, draggableElement);
    });
  }
  
  void _installDropzoneListeners(Element dropzoneElement) {
    // Keep track of elements where dragEnter or dragLeave has been fired on.
    // This is necessary as a dragEnter or dragLeave event is not only fired
    // on the [dropzoneElement] but also on its children. Now, whenever the 
    // [dragOverElements] is empty we know the dragEnter or dragLeave event
    // was fired on the real [dropzoneElement].
    Set<Element> dragOverElements = new Set<Element>();
    
    // Drag Enter.
    dropzoneElement.onDragEnter.listen((MouseEvent event) {
      // This is necessary for IE.
      event.preventDefault();
      
      // Only handle the dropzone element and not on any of its children.
      if (dragOverElements.length == 0) {
        dropzoneElement.classes.add(CSS_CLASS_OVER);
                
        _fireEvent(_onDragEnterController, _currentDragElement, dropzoneElement);
      }
      dragOverElements.add(event.target);
    });
    
    // Drag Over.
    dropzoneElement.onDragOver.listen((MouseEvent event) {
      // This is necessary to allow us to drop.
      event.preventDefault();
      event.dataTransfer.dropEffect = 'move';
      
      _fireEvent(_onDragOverController, _currentDragElement, dropzoneElement);
    });
    
    // Drag Leave.
    dropzoneElement.onDragLeave.listen((MouseEvent event) {
      // Firefox fires too many onDragLeave events. This condition fixes it. 
      if (event.target != event.relatedTarget) {
        dragOverElements.remove(event.target);
      }
      
      // Only handle the dropzone element and not on any of its children.
      if (dragOverElements.length == 0) {
        dropzoneElement.classes.remove(CSS_CLASS_OVER);
        
        _fireEvent(_onDragLeaveController, _currentDragElement, dropzoneElement);
      }
    });
    
    // Drop.
    dropzoneElement.onDrop.listen((MouseEvent event) {
      // Stop the browser from redirecting.
      event.preventDefault();
      
      dropzoneElement.classes.remove(CSS_CLASS_OVER);
      
      dragOverElements.clear();
      
      _fireEvent(_onDropController, _currentDragElement, dropzoneElement);
    });
  }
  
  Stream<DragAndDropEvent> get onDragStart => _onDragStartController.stream;
  Stream<DragAndDropEvent> get onDragEnd => _onDragEndController.stream;
  Stream<DragAndDropEvent> get onDragEnter => _onDragEnterController.stream;
  Stream<DragAndDropEvent> get onDragOver => _onDragOverController.stream;
  Stream<DragAndDropEvent> get onDragLeave => _onDragLeaveController.stream;
  Stream<DragAndDropEvent> get onDrop => _onDropController.stream;
  
  /**
   * Fires a [DragAndDropEvent] on [streamController]. The [dragElement]
   * must always be provided. The [dropzoneElement] is optional.
   */
  void _fireEvent(BroadcastStreamController streamController, 
                  Element dragElement, [Element dropzoneElement = null]) {
    if (streamController.hasListener && !streamController.isClosed 
        && !streamController.isPaused) {
      
      streamController.add(new DragAndDropEvent(dragElement, dropzoneElement));
    }
  }
  
  /**
   * Moves the drag element to the position of the dropzone element. If the 
   * dropzone element is null, it will be moved to the first position of its parent.
   */
  static void move(DragAndDropEvent event) {
    if (event.dragElement == event.dropzoneElement) {
      return;
    }
    
    if (event.dropzoneElement == null) {
      // Insert in first position of its parent.
      Element parent1 = event.dragElement.parent;
      event.dragElement.remove();
      parent1.children.insert(0, event.dragElement);      
    } else {
      // Insert at position of [e2].
      Element parent2 = event.dropzoneElement.parent;
      int index2 = _getElementIndexInParent(event.dropzoneElement);
      
      event.dragElement.remove();
      parent2.children.insert(index2, event.dragElement);      
    }
  }
  
  /**
   * Swaps dragElement and drozoneElement.
   */
  static void swap(DragAndDropEvent event) {
    Element e1 = event.dragElement;
    Element e2 = event.dropzoneElement;
    
    if (e1 == e2) {
      return;
    }
    
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
  static int _getElementIndexInParent(Element e1) {
    int index = 0;
    var previous = e1.previousElementSibling;
    while (previous != null) {
      index++;
      previous = previous.previousElementSibling;
    }
    return index;
  }
  
//  /**
//   * Lazy initialization of a [BroadcastStreamController].
//   */
//  BroadcastStreamController<DragAndDropEvent> _lazyInit(
//      BroadcastStreamController<DragAndDropEvent> controller) {
//    if (controller == null) {
//      controller = new BroadcastStreamController();
//    }
//    return controller;
//  }
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
  
  DragAndDropEvent(this.dragElement, this.dropzoneElement);
}




