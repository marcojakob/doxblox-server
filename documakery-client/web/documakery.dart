import 'dart:html';
import 'package:web_ui/web_ui.dart';
import 'dart:async';

import 'ui/split_panel.dart';
import 'ui/navigation/navigation_view.dart';
import 'ui/digest/digest_view.dart';
import 'ui/editor/editor_view.dart';

import 'model/document.dart';

Element header;
SplitPanelContainer baseContainer;

Element navigationView;
Element digestView;
Element editorView;

void init() {
  header = query("#header");
  
  navigationView = query('#navigation-view');
  digestView = query('#digest-view');
  editorView = query('#editor-view');
  
  DivElement baseContainerElement = query("#split-container");
  
  List<SplitPanel> panels = new List();
  panels.add(new SplitPanel(navigationView, 0.2));
  panels.add(new SplitPanel(digestView, 0.2));
  panels.add(new SplitPanel(editorView, 0.6));
  
  baseContainer = new SplitPanelContainer(baseContainerElement, panels, stackedVertical: false);
  
  // Let the split panel fill in the entire screen
  window.onResize.listen(_onResized);
  _onResized(null); // call resize for the first time
}

void _onResized(Event event) {
  // Leave some space for the header
  int headerHeight = header.clientHeight;
  baseContainer.containerElement.style.paddingTop = "${headerHeight}px";
  baseContainer.resize(window.innerWidth, window.innerHeight - headerHeight);
}

void main() {
  // defer until the end of the event loop so that web components are loaded first
  Timer.run(init);
}