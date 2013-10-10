library doxblox.split_panel_child;

import 'package:polymer/polymer.dart';

/**
 * A child panel of the split panel.
 */
@CustomTag('doxblox-split-panel-child')
class SplitPanelChildElement extends PolymerElement {
  
  /// Defines the initial size of the panel, relative to the other panels.
  @published num ratio = 1;
  
  int _cachedWidth = 0;
  int _cachedHeight = 0;
  
  int get width => _cachedWidth;
  set width(int value) {
    if (_cachedWidth != value) {
      _cachedWidth = value;
      style.width = "${value}px";
    }
  }
  
  int get height => _cachedHeight;
  set height(int value) {
    if (_cachedHeight != value) {
      _cachedHeight = value;
      style.height = "${value}px";
    }
  }
}