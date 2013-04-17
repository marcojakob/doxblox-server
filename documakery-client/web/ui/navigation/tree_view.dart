import 'dart:async';
import 'package:web_ui/web_ui.dart';
import 'package:js/js.dart' as js;

/**
 * A TreeView widget that wraps jsTree.
 * 
 * TODO: Replace jsTree with a Dart tree implementation.
 */
class TreeView extends WebComponent {
  
  Stream<String> _onNodeSelect;
  
  /**
   * Lifecycle method invoked whenever a component is added to the DOM.
   */
  inserted() {
    _initJSTree();
  }
  
  /**
   * Initialize the jsTree.
   */
  void _initJSTree() {
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
    
    js.context.jQuery('#tree-host').jstree(options);
  }
  
  /**
   * Returns the stream of selected nodes. The stream contains the id attribute of the selected nodes.
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
      js.context.jQuery('#tree-host').bind('select_node.jstree', nodeSelectCallback);  
      
      _onNodeSelect = controller.stream;
    }
    return _onNodeSelect;
  }
}



