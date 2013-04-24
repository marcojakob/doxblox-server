library tree_view;

import 'dart:async';
import 'dart:json' as json;
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
  
  /**
   * Initialize the jsTree.
   */
  void initTree(TreeNode rootNode) {
    var options = js.map({
      "plugins" : ["themes","json_data","ui","crrm","hotkeys","dnd","types"], 
      "json_data" : {
        "data" : rootNode.toJson(),
      },
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
      },
      "types" : {
        "valid_children" : [ "folder" ],
        "types" : {
          // the 'document' type
          "document" : {
            "valid_children" : "none",
            "icon" : {
              "image" : "../resources/file.png"
            },
            "select_node" : true,
            "open_node" : false
          },
          // the 'folder' type
          "folder" : {
            "valid_children" : [ "document", "folder" ],
            "icon" : {
              "image" : "../resources/folder.png"
            },
            "select_node" : false,
            "open_node" : true
          }
        }
      }
    });
    
    js.context.jQuery(_root).jstree(options);
  }
  
  /**
   * Adds a node with specified [id], [text] and [type] to the parent node 
   * identified by [parentId] and [parentType].
   */
  void addNode(String parentId, String parentType, String id, String text, String type) {
    TreeNode newNode = new TreeNode(id, text, type);
    var newNodeMap = js.map(newNode.toJson());
    
    var parentNode = js.context.jQuery('#${parentId}[rel="${parentType}"]');
    if (parentNode != null) {
      js.context.jQuery(_root).jstree('create', parentNode, 'inside',  
          newNodeMap, false, true);
    }
  }
  
  /**
   * Adds a node with specified [id], [text] and [type] to the parent javascript 
   * node [parentJsNode].
   */
  void addNodeToParentJsNode(var parentJsNode, String id, String text, String type) {
    TreeNode newNode = new TreeNode(id, text, type);
    var newNodeMap = js.map(newNode.toJson());
    
    if (parentJsNode != null) {
      js.context.jQuery(_root).jstree('create', parentJsNode, 'inside',  
          newNodeMap, false, true);
    }
  }
  
  /**
   * Returns a stream of selected nodes. The stream sends data events 
   * containing the javascript node that was opened. 
   * To get the id attribute of the javascript node, for example, use `.attr('id')`.
   */
  Stream<js.Proxy> onSelectNode() {
    StreamController<js.Proxy> controller = new StreamController<js.Proxy>();
    
    // Create a JavaScript callback that forwards to the stream controller.
    var selectNodeCallback = new js.Callback.many((event, data) {
      controller.add(data.rslt.obj);
    });
    
    // Bind the callback function to the event.
    js.context.jQuery(_root).on('select_node.jstree', selectNodeCallback);  
  
    return controller.stream;
  }
  
  /**
   * Returns a stream of opened nodes. The stream sends data events 
   * containing the javascript node that was opened. 
   * To get the id attribute of the javascript node, for example, use `.attr('id')`.
   */
  Stream<js.Proxy> onOpenNode() {
    StreamController<js.Proxy> controller = new StreamController<js.Proxy>();
    
    // Create a JavaScript callback that forwards to the stream controller.
    var openNodeCallback = new js.Callback.many((event, data) {
      controller.add(data.rslt.obj);
    });
    
    // Bind the callback function to the event.
    js.context.jQuery(_root).on('open_node.jstree', openNodeCallback);  
    
    return controller.stream;
  }
}

/**
 * Tree node that can be converted to json for jsTree.
 */
class TreeNode {
  String id;
  String text;
  String type;
  List<TreeNode> children = new List();
  
  TreeNode(this.id, this.text, this.type);
  
  /// Converts this [TreeNode] to a JSON map.
  Map toJson() {
    return {
      'data' : text,
      'attr' : { 
        'id' : id, 
        'rel' : type
      },
      // Apply toJson() to all the children.
      'children' : children.map((TreeNode node) => node.toJson()).toList()
    };
  }
}



