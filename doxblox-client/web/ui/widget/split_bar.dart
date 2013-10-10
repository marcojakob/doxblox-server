library doxblox.split_bar;

import 'package:polymer/polymer.dart';
import 'dart:html';
import 'dart:async';
import 'package:logging/logging.dart';

import 'split_panel.dart';
import 'split_panel_child.dart';

final _log = new Logger("doxblox.split_bar");


/**
 * The splitter between two [SplitPanelChildElement]s panels.
 */
@CustomTag('doxblox-split-bar')
class SplitBarElement extends PolymerElement {
  
  /// The parent split panel.
  SplitPanelElement parent;
  
  /// The panel to the left/top side of the bar, depending on the bar orientation
  SplitPanelChildElement previousPanel; 
  
  /// The panel to the right/bottom side of the bar, depending on the bar orientation
  SplitPanelChildElement nextPanel;     
  
  StreamSubscription<MouseEvent> mouseMovedHandler;
  StreamSubscription<MouseEvent> mouseDownHandler;
  StreamSubscription<MouseEvent> mouseUpHandler;
  MouseEvent previousMouseEvent;
  
  int get width => client.width;
  int get height => client.height;
  
  /**
   * Sets the size of the bar. If split vertically the size is the height, if 
   * split horizontally the size is the width.
   */
  set size(int value) {
    if (parent.orientation == HORIZONTAL) {
      style.height = '${value}px';
    } else {
      style.width = '${value}px';
    }
  }
  
  bool readyToProcessNextDrag = true;
  
  void _startDragging(Event e, var detail, Element target) {
    document.body.classes.add('disable-selection');
    if (mouseMovedHandler != null) {
      mouseMovedHandler.cancel();
      mouseMovedHandler = null;
    }
    if (mouseUpHandler != null) {
      mouseUpHandler.cancel();
      mouseUpHandler = null;
    }
    mouseMovedHandler = window.onMouseMove.listen(_onMouseMoved);
    mouseUpHandler = window.onMouseUp.listen(_stopDragging);
    previousMouseEvent = e;
  }
  
  void _stopDragging(Event e, [var detail, Element target]) {
    document.body.classes.remove('disable-selection');
    if (mouseMovedHandler != null) {
      mouseMovedHandler.cancel();
      mouseMovedHandler = null;
    }
    if (mouseUpHandler != null) {
      mouseUpHandler.cancel();
      mouseUpHandler = null;
    }
  }
  
  void _onMouseMoved(MouseEvent e) {
    int dx = e.page.x - previousMouseEvent.page.x;
    int dy = e.page.y - previousMouseEvent.page.y;
    previousMouseEvent = e;
    
    // Ignore if mouse is on the wrong side of the split bar, e.g. a drag to 
    // the right should only be performed if the mouse is to the right of the
    // split bar.
    if (parent.orientation == HORIZONTAL) {
      if (dx < 0) { // Dragging left.
        if (e.page.x > documentOffset.x + width) { // Mouse is right of bar.
          return;
        }
      } else { // Dragging right.
        if (e.page.x < documentOffset.x) { // Mouse is left of bar.
          return;
        }
      }
    } else { // VERTICAL
      if (dy < 0) { // Dragging up.
        if (e.page.y > documentOffset.y + height) { // Mouse is below bar.
          return;
        }
      } else { // Dragging down.
        if (e.page.y < documentOffset.y) { // Mouse is above bar.
          return;
        }
      }
    }
    
    _performDrag(dx, dy);
  }
  
  void _performDrag(int dx, int dy) {
    bool horizontal = parent.orientation == HORIZONTAL;
    
    int previousPanelSize = horizontal ? previousPanel.width : previousPanel.height;
    int nextPanelSize = horizontal ? nextPanel.width : nextPanel.height;
    int totalSize = previousPanelSize + nextPanelSize;
    
    int deltaMovement = horizontal ? dx : dy;
    int newPreviousPanelSize = previousPanelSize + deltaMovement; 
    int newNextPanelSize = nextPanelSize - deltaMovement;
    
    int minSize = MIN_PANEL_SIZE;
    if (totalSize < 2 * MIN_PANEL_SIZE) {
      // Not enough space for both panels to have the minimum panel size.
      // Make min size 30% of total size.
      minSize = (totalSize * 0.3).toInt();
    }
    
    // Prevent resizing if it makes a panel smaller than min size.
    if (newPreviousPanelSize < previousPanelSize && newPreviousPanelSize < minSize) {
      newPreviousPanelSize = minSize;
      newNextPanelSize = totalSize - newPreviousPanelSize;
    } else if (newNextPanelSize < nextPanelSize && newNextPanelSize < minSize) {
      newNextPanelSize = minSize;
      newPreviousPanelSize = totalSize - newNextPanelSize;
    }

    if (horizontal) {
      previousPanel.width = newPreviousPanelSize;
      nextPanel.width = newNextPanelSize;
    } else {
      previousPanel.height = newPreviousPanelSize;
      nextPanel.height = newNextPanelSize;
    }
  }
}