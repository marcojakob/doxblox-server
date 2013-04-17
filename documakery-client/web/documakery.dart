import 'dart:html';
import 'package:web_ui/web_ui.dart';
import 'dart:async';
import 'ui/split_panel.dart';
import 'ui/navigation/tree_view.dart';

void main() {
  // defer until the end of the event loop so that web components are loaded first
  Timer.run(init);
}

init() {
  TreeView documentTree = query('#document-tree').xtag;
  documentTree.onNodeSelect.listen((id) => print(id));
  new LayoutManager();
}

class LayoutManager {
  Element header;
  SplitPanelContainer baseContainer;
  
  LayoutManager() {
    header = query("#header");
    
    List<SplitPanel> panels = new List();
    panels.add(new SplitPanel(query("#content-navigation"), 0.2));
    panels.add(new SplitPanel(query("#content-digest"), 0.2));
    panels.add(new SplitPanel(query("#content-editor"), 0.6));
    
    baseContainer = new SplitPanelContainer(query("#split-container"), panels, stackedVertical: false);
    
    // Let the split panel fill in the entire screen
    window.onResize.listen(onResized);
    onResized(null); // call resize for the first time
  }
  
  void onResized(Event event) {
    // Leave some space for the header
    int headerHeight = header.clientHeight;
    baseContainer.containerElement.style.paddingTop = "${headerHeight}px";
    baseContainer.resize(window.innerWidth, window.innerHeight - headerHeight);
  }
}

