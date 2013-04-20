library tree_view;

import 'dart:async';
import 'package:web_ui/web_ui.dart';
import 'package:js/js.dart' as js;

/**
 * A TreeView widget that wraps jsTree.
 * 
 * The following javascript libraries must be present:
 * * jquery.jstree.js (version 1.0)
 * * jquery.js
 * * jquery.hotkeys.js
 * 
 * TODO: Replace jsTree with a Dart tree implementation.
 */
class TreeView extends WebComponent {
  
  Stream<String> _onNodeSelect;
  
  /**
   * Initialize the jsTree.
   */
  void initTree() {
    var options = js.map({
      "plugins" : ["themes","html_data","ui","crrm","hotkeys","dnd"], 
      "core" : { 
        "animation" : 0
      },
      "themes" : {
        "theme" : "custom",
        "dots" : false,
        "icons" : true
      },
      "ui" : {
          "select_limit" : 1
      }
    });
    
    js.context.jQuery(_root).jstree(options);
  }
  
  /**
   * Returns the stream of selected nodes. The stream contains the id attribute 
   * of the selected nodes.
   */
  Stream<String> get onNodeSelect {
    // lazy initialization
    if (_onNodeSelect == null) {
      StreamController<String> controller = new StreamController<String>();
      
      // Create a JavaScript callback that forwards to the stream controller.
      var nodeSelectCallback = new js.Callback.many((event, data) {
        controller.add(data.rslt.obj.attr('id'));
      });
      
      // Bind the callback function to the select node event.
      js.context.jQuery(_root).bind('select_node.jstree', nodeSelectCallback);  
      
      _onNodeSelect = controller.stream;
    }
    return _onNodeSelect;
  }
}



