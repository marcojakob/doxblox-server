library doxblox.split_panel;

import 'split_panel_child.dart';
import 'split_bar.dart';

import 'package:polymer/polymer.dart';
import 'dart:math' as math;

/// The horizontal (right <-> left) orientation.
const String HORIZONTAL = "horizontal";

/// The vertical (top <-> bottom) orientation.
const String VERTICAL = "vertical";

/// Minimum size of a child panel.
const int MIN_PANEL_SIZE = 80; 

/**
 * A panel that adds user-positioned splitters between each of its child panels.
 * It can be stacked horizontally or vertically.
 */
@CustomTag('doxblox-split-panel')
class SplitPanelElement extends PolymerElement {
  
  /// The orientation of the split panel. Child panels can be positioned 
  /// horizontally next to each other, or stacked vertically.
  @published String orientation = HORIZONTAL;
  
  List<SplitPanelChildElement> childPanels;
  List<SplitBarElement> splitBars;
  
  void created() {
    super.created();
    // Get all direct children that were passed to the component
    // only SplitPanelChild components are allowed
    childPanels = children
        .where((child) => child.localName == 'doxblox-split-panel-child')
        .map((panel) => panel.xtag).toList(growable: false);
    
    // Set orientation as CSS class on all child panels.
    childPanels.forEach((panel) => panel.classes.add(orientation));
    
    _createSplitBars();
  }
  
  /**
   * Initialize split bars between the panels.
   */
  void _createSplitBars() {
    splitBars = new List<SplitBarElement>();
    
    // No split bar if there is zero or one child panel.
    if (childPanels.length <= 1) return;
    
    for (int i = 0; i < childPanels.length - 1; i++) {
      var previousPanel = childPanels[i];
      var nextPanel = childPanels[i + 1];
      var bar = createElement('doxblox-split-bar');
      SplitBarElement barElement = bar.xtag;
      barElement..parent = this
          ..previousPanel = previousPanel
          ..nextPanel = nextPanel
          ..classes.add(orientation); // Add orientation as CSS class.
      splitBars.add(barElement);
      
      // Insert split bar into (light?) dom.
      children.insert(i * 2 + 1, bar);
    }
  }
  
  /**
   * Resizes the child panels according to their initial relative ratios.
   * The [width] and [height] must be the size of the content (without padding 
   * and border).
   */ 
  void resizeWithRatios(int width, int height) {
    if (childPanels.length <= 1) return;
    
    bool horizontal = orientation == HORIZONTAL;
    
    // Get the sum of all child panel ratios.
    num totalRatios = 0;
    childPanels.forEach((panel) {
      totalRatios += panel.ratio;
    });
    
    int containerSize = horizontal ? width : height;
    int barSize = horizontal ? splitBars[0].width : splitBars[0].height;
    int totalBarSize = splitBars.length * barSize;
    
    int panelSizeQuota = containerSize - totalBarSize;
    
    childPanels.forEach((panel) {
      num size = (panel.ratio / totalRatios) * panelSizeQuota;
      
      if (horizontal) {
        panel.width = size.toInt();
      } else {
        panel.height = size.toInt();
      }
    });
    
    style.width = "${width}px";
    style.height = "${height}px";
  }

  /**
   * Resizes the container and it's children. The [width] and [height] must be
   * the size of the content (without padding and border).
   */
  void resize(int width, int height) {
    if (childPanels.length <= 1) return;
    
    bool horizontal = orientation == HORIZONTAL;
    
    // Find out how much space existing child panels take up (excluding the splitter bars)
    int totalChildPanelSize = 0;
    childPanels.forEach((panel) {
      int size = horizontal ? panel.width : panel.height;
      totalChildPanelSize += size;
    });
    
    // Get the thickness of the bar
    int barSize = horizontal ? splitBars[0].width : splitBars[0].height;
    
    // Find out how much space existing child panels will take after being resized (excluding the split bars)  
    int targetTotalChildPanelSize = horizontal ? width : height;
    targetTotalChildPanelSize -= barSize * splitBars.length;
    
    // Get the scale multiplier 
    totalChildPanelSize = math.max(totalChildPanelSize, 1); // Make 
    num scaleMultiplier = targetTotalChildPanelSize / totalChildPanelSize;
    
    // Update the size with the multiplier.
    int updatedTotalChildPanelSize = 0;
    childPanels.forEach((child) {
      int originalSize = horizontal ? child.width : child.height;
      int newSize = (originalSize * scaleMultiplier).round();
      updatedTotalChildPanelSize += newSize;
      
      // Set the size of the panel
      if (horizontal) {
        child.width = newSize;
      } else {
        child.height = newSize;
      }
    });
    
    // Fix the rounding off errors and match the requested size
    int roundingError = targetTotalChildPanelSize - updatedTotalChildPanelSize;
    if (roundingError != 0) {
      // Add rounding error pixels to a panel. Panels receiving the pixels should take turns. 
      var childIndex = targetTotalChildPanelSize % (childPanels.length);
      SplitPanelChildElement child = childPanels[childIndex];
      if (horizontal) {
        child.width += roundingError;
      } else {
        child.height += roundingError;
      }
    }
    
    style.width = "${width}px";
    style.height = "${height}px";
  }
}