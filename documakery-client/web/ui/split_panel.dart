library split_panel;

import 'dart:html';
import 'dart:math' as math;
import 'dart:async';

/**
 * A panel that adds user-positioned splitters between each of its child panels.
 * It can be stacked horizontally or vertically.
 * 
 * This is a simplified version of the splitter_panel from [Dock Spawn](http://www.dockspawn.com/).
 */  
class SplitPanelContainer {
  const String CSS_SPLIT_PANEL_VERTICAL = "splitpanel-vertical";
  const String CSS_SPLIT_PANEL_HORIZONTAL = "splitpanel-horizontal";
  
  DivElement containerElement;
  List<SplitPanel> childPanels;
  List<SplitBar> splitBars;
  bool stackedVertical;
  
  SplitPanelContainer(this.containerElement, this.childPanels, {this.stackedVertical: false}) {
    splitBars = new List<SplitBar>();
    _buildSplitPanelDOM();
    performLayoutWithRatios();
  }
  
  void _buildSplitPanelDOM() {
    if (childPanels.length <= 1) return;
    
    splitBars = new List<SplitBar>();
    for (int i = 0; i < childPanels.length - 1; i++) {
      var previousPanel = childPanels[i];
      var nextPanel = childPanels[i + 1];
      var splitterBar = new SplitBar(previousPanel, nextPanel, stackedVertical);
      splitBars.add(splitterBar);
      
      // Add the div element and split bar to the panel's base div element
      _insertIntoBaseElement(previousPanel);
      containerElement.nodes.add(splitterBar.barElement);
    }
    _insertIntoBaseElement(childPanels.last);
  }

  void _insertIntoBaseElement(SplitPanel panel) {
    panel.contentElementHost.remove();
    containerElement.nodes.add(panel.contentElementHost);
    panel.contentElementHost.classes.add(stackedVertical ? CSS_SPLIT_PANEL_VERTICAL : CSS_SPLIT_PANEL_HORIZONTAL);
  }
  
  /**
   * Resizes the child panels according to their initial relative ratios.
   */ 
  void performLayoutWithRatios() {
    if (childPanels.length <= 1) return;
    
    // Get the sum of all child panel ratios.
    num totalRatios = 0;
    childPanels.forEach((panel) {
      totalRatios += panel.ratio;
    });
    
    int containerSize = stackedVertical ? containerElement.clientHeight : containerElement.clientWidth;
    int barSize = stackedVertical ? splitBars[0].height : splitBars[0].width;
    int totalBarSize = splitBars.length * barSize;
    
    int panelSizeQuota = containerSize - totalBarSize;
    
    childPanels.forEach((panel) {
      num size = (panel.ratio / totalRatios) * panelSizeQuota;
      
      if (stackedVertical) {
        panel.height = size.toInt();
      } else {
        panel.width = size.toInt();
      }
    });
  }

  /**
   * Resizes the container and it's children.
   */
  void resize(int width, int height) {
    if (childPanels.length <= 1) return;
    
    // Adjust the fixed dimension that is common to all (i.e. width, if stacked vertical; height, if stacked horizontally)
    for (int i = 0; i < childPanels.length; i++) {
      var childPanel = childPanels[i];
      if (stackedVertical) {
        childPanel.width = width;
      } else {
        childPanel.height = height;
      }
      
      if (i < splitBars.length) {
        var splitBar = splitBars[i];
        if (stackedVertical) {
          splitBar.width = width;
        } else {
          splitBar.height = height;
        }
      }
    }
    
    // Adjust the varying dimension
    int totalChildPanelSize = 0;
    // Find out how much space existing child panels take up (excluding the splitter bars)
    childPanels.forEach((panel) {
      int size = stackedVertical ? panel.height : panel.width;
      totalChildPanelSize += size;
    });
    
    // Get the thickness of the bar
    int barSize = stackedVertical ? splitBars[0].height : splitBars[0].width;
    
    // Find out how much space existing child panels will take after being resized (excluding the split bars)  
    int targetTotalChildPanelSize = stackedVertical ? height : width;
    targetTotalChildPanelSize -= barSize * splitBars.length;
    
    // Get the scale multiplier 
    totalChildPanelSize = math.max(totalChildPanelSize, 1);
    num scaleMultiplier = targetTotalChildPanelSize / totalChildPanelSize;
    
    // Update the size with this multiplier
    int updatedTotalChildPanelSize = 0;
    
    childPanels.forEach((child) {
      int originalSize = stackedVertical ? child.height : child.width;
      
      int newSize = (originalSize * scaleMultiplier).round();
      updatedTotalChildPanelSize += newSize;
      
      // Set the size of the panel
      if (stackedVertical) {
        child.height = newSize;
      } else {
        child.width = newSize;
      }
    });
    
    // Fix the rounding off errors and match the requested size
    int roundingError = targetTotalChildPanelSize - updatedTotalChildPanelSize;
    if (roundingError != 0) {
      // Add rounding error pixels to a panel. Panels receiving the pixels should take turns. 
      var childIndex = targetTotalChildPanelSize % (childPanels.length);
      SplitPanel child = childPanels[childIndex];
      if (stackedVertical) {
        child.height += roundingError;
      } else {
        child.width += roundingError;
      }
    }
    
    containerElement.style.width = "${width}px";
    containerElement.style.height = "${height}px";
  }
}

/**
 * A [SplitPanel] contains a [DivElement] with the panels content and a ratio 
 * for the relative size to other panels.
 */
class SplitPanel {
  // User provided container element to be placed in the panel's content area
  DivElement contentElement;
  // Host element containing the content element.
  DivElement contentElementHost;
  
  final num ratio;
  
  int _cachedWidth = 0;
  int _cachedHeight = 0;
  
  /**
   * Creates a [SplitPanel] for the [contentElement]. The [ratio] determines 
   * the initial size, relative to other panels. 
   */
  SplitPanel(this.contentElement, this.ratio) {
    contentElementHost = new DivElement();
    contentElementHost.nodes.add(contentElement);
  }
  
  int get width => _cachedWidth;
  set width(int value) {
    if (_cachedWidth != value) {
      _cachedWidth = value;
      contentElementHost.style.width = "${value}px";
    }
  }
  
  int get height => _cachedHeight;
  set height(int value) {
    if (_cachedHeight != value) {
      _cachedHeight = value;
      contentElementHost.style.height = "${value}px";
    }
  }
}

/**
 * The splitter between two child [DivElement]s.
 * 
 * [SplitBar] is inspired by the splitter_bar of [Dock Spawn](http://www.dockspawn.com/).
 */
class SplitBar {
  const String CSS_SPLIT_BAR_HORIZONTAL = "splitbar-horizontal";
  const String CSS_SPLIT_BAR_VERTICAL = "splitbar-vertical";
  const String CSS_DISABLE_SELECTION = "disable-selection";
  
  SplitPanel previousPanel; // The panel to the left/top side of the bar, depending on the bar orientation
  SplitPanel nextPanel;     // The panel to the right/bottom side of the bar, depending on the bar orientation
  DivElement barElement;
  bool stackedVertical;
  StreamSubscription<MouseEvent> mouseMovedHandler;
  StreamSubscription<MouseEvent> mouseDownHandler;
  StreamSubscription<MouseEvent> mouseUpHandler;
  MouseEvent previousMouseEvent;
  int minPanelSize = 50; // TODO: Get from container configuration
  
  SplitBar(this.previousPanel, this.nextPanel, this.stackedVertical) {
    barElement = new DivElement();
    barElement.classes.add(stackedVertical ? CSS_SPLIT_BAR_HORIZONTAL : CSS_SPLIT_BAR_VERTICAL);
    
    mouseDownHandler = barElement.onMouseDown.listen(onMouseDown);
  }
  
  int get width => barElement.clientWidth;
  set width(int value) => barElement.style.width = "${value}px";

  int get height => barElement.clientHeight;
  set height(int value) => barElement.style.height = "${value}px";
  
  void onMouseDown(MouseEvent e) {
    _startDragging(e);
  }
  
  void onMouseUp(MouseEvent e) {
    _stopDragging(e);
  }
 
  bool readyToProcessNextDrag = true;
  void onMouseMoved(MouseEvent e) {
    if (!readyToProcessNextDrag) {
      print ("Skip");
      return;
    }
    readyToProcessNextDrag = false;
//    var dockManager = previousPanel.dockManager;
//    dockManager.suspendLayout();
    int dx = e.page.x - previousMouseEvent.page.x;
    int dy = e.page.y - previousMouseEvent.page.y;
    _performDrag(dx, dy);
    previousMouseEvent = e;
    readyToProcessNextDrag = true;
//    dockManager.resumeLayout();
  }
  
  void _performDrag(int dx, int dy) {
    int previousWidth = previousPanel.width;
    int previousHeight = previousPanel.height;
    int nextWidth = nextPanel.width;
    int nextHeight = nextPanel.height;
    
    int previousPanelSize = stackedVertical ? previousHeight : previousWidth; 
    int nextPanelSize = stackedVertical ? nextHeight : nextWidth;
    int deltaMovement = stackedVertical ? dy : dx;
    int newPreviousPanelSize = previousPanelSize + deltaMovement; 
    int newNextPanelSize = nextPanelSize - deltaMovement;
    
    if (newPreviousPanelSize < minPanelSize || newNextPanelSize < minPanelSize) {
      // One of the panels is smaller than it should be.
      // In that case, check if the small panel's size is being increased
      bool continueProcessing = (newPreviousPanelSize < minPanelSize && newPreviousPanelSize > previousPanelSize) || 
          (newNextPanelSize < minPanelSize && newNextPanelSize > nextPanelSize);
      
      if (!continueProcessing) return;
    }

    if (stackedVertical) {
      previousPanel.width = previousWidth;
      previousPanel.height = newPreviousPanelSize;
      nextPanel.width = nextWidth;
      nextPanel.height = newNextPanelSize;
    } else {
      previousPanel.width = newPreviousPanelSize;
      previousPanel.height = previousHeight;
      nextPanel.width = newNextPanelSize;
      nextPanel.height = nextHeight;
    }
  }
  
  void _startDragging(MouseEvent e) {
//    disableGlobalTextSelection();
    document.body.classes.add(CSS_DISABLE_SELECTION);
    if (mouseMovedHandler != null) {
      mouseMovedHandler.cancel();
      mouseMovedHandler = null;
    }
    if (mouseUpHandler != null) {
      mouseUpHandler.cancel();
      mouseUpHandler = null;
    }
    mouseMovedHandler = window.onMouseMove.listen(onMouseMoved);
    mouseUpHandler = window.onMouseUp.listen(onMouseUp);
    previousMouseEvent = e;
  }
  
  void _stopDragging(MouseEvent e) {
//    enableGlobalTextSelection();
    document.body.classes.remove(CSS_DISABLE_SELECTION);
    if (mouseMovedHandler != null) {
      mouseMovedHandler.cancel();
      mouseMovedHandler = null;
    }
    if (mouseUpHandler != null) {
      mouseUpHandler.cancel();
      mouseUpHandler = null;
    }
  }
}

