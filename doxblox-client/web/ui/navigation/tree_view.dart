library doxblox.tree_view;

import 'dart:async';
import 'dart:json' as json;
import 'package:meta/meta.dart';
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
  
  @observable
  TreeNode rootNode;
  
  /// The currently selected node
  TreeNode selectedNode;
  
  /**
   * Invoked when component is added to the DOM.
   */
  inserted() {
    if (rootNode != null) {
      initTree(rootNode);
    }
    
    // Create observer to watch for rootNode changes.
    observe(() => rootNode, (ChangeNotification e) {
      initTree(rootNode);
    });
  }
  
  
  /**
   * Initialize the jsTree.
   */
  void initTree(TreeNode rootNode) {
    var selectNodeCallback = new js.Callback.many(($this, event) {
      if($this.data.ui.hovered != null) { 
        $this.data.ui.hovered.children("a:eq(0)").click(); 
      } 
      return false; // means do not bubble event up to browser
    }, withThis: true);
    
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
        "valid_children" : [ TreeNode.FOLDER_TYPE ],
        "types" : {
          // the 'document' type
          "${TreeNode.DOCUMENT_TYPE}" : {
            "valid_children" : "none",
            "icon" : {
              "image" : "../resources/file.png"
            },
            "select_node" : true,
            "open_node" : false
          },
          // the 'folder' type
          "${TreeNode.FOLDER_TYPE}" : {
            "valid_children" : [ TreeNode.DOCUMENT_TYPE, TreeNode.FOLDER_TYPE ],
            "icon" : {
              "image" : "../resources/folder.png"
            },
            "select_node" : true,
            "open_node" : true
          }
        }
      },
      "hotkeys" : {
        "return" : selectNodeCallback
      }
    });
    js.context.jQuery(host).jstree(options);
    
    // Bind to the loaded event and select node when it is loaded. Needed in 
    // case the tree was not loaded when a node was selected.
    var loadedCallback = new js.Callback.once((event, data) {
      if (selectedNode != null) {
        js.context.jQuery(host).jstree('select_node', '#${selectedNode.id}[rel="${selectedNode.type}"]', true);
      }
    });
    js.context.jQuery(host).on('loaded.jstree', loadedCallback);
    
    // Use double-click event to open/close a node
    var openCloseNodeCallback = new js.Callback.many((event) {
      var node = js.context.jQuery(event.target).closest("li");
      js.context.jQuery(host).jstree('toggle_node', node);
    });
    js.context.jQuery(host).on("dblclick.jstree", openCloseNodeCallback);
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
      js.context.jQuery(host).jstree('create', parentNode, 'inside',  
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
      js.context.jQuery(host).jstree('create', parentJsNode, 'inside',  
          newNodeMap, false, true);
    }
  }
  
  /**
   * Selects the node with [id] and [type] inside the tree.
   */
  void selectNode(String id, String type) {
    var newSelectedNodeJs = js.context.jQuery('#${id}[rel="${type}"]');
    String text = newSelectedNodeJs.attr('data');
    TreeNode newSelectedNode = new TreeNode(id, text, type);
    
    // Prevent firing select event in a loop.
    if (selectedNode != newSelectedNode) {
      selectedNode = newSelectedNode;
      js.context.jQuery(host).jstree('select_node', '#${id}[rel="${type}"]', true);
    }
  }
  
  /**
   * Returns a stream of selected nodes. The stream sends data events 
   * containing the [TreeNode] node that was selected.
   */
  Stream<TreeNode> onSelectNode() {
    StreamController<TreeNode> controller = new StreamController<TreeNode>();
    
    // Create a JavaScript callback that forwards to the stream controller.
    var selectNodeCallback = new js.Callback.many((event, data) {
      var newSelectedNodeJs = data.rslt.obj;
      String id = newSelectedNodeJs.attr('id');
      String text = newSelectedNodeJs.attr('data');
      String type = newSelectedNodeJs.attr('rel');
      TreeNode newSelectedNode = new TreeNode(id, text, type);
      
      // Prevent firing select event in a loop.
      if (selectedNode != newSelectedNode) {
        selectedNode = newSelectedNode;
        controller.add(newSelectedNode);
      }
    });
    
    // Bind the callback function to the event.
    js.context.jQuery(host).on('select_node.jstree', selectNodeCallback);  
  
    return controller.stream;
  }
  
  /**
   * Returns a stream of opened nodes. The stream sends data events 
   * containing the [TreeNode] node that was opened. 
   */
  Stream<TreeNode> onOpenNode() {
    StreamController<TreeNode> controller = new StreamController<TreeNode>();
    
    // Create a JavaScript callback that forwards to the stream controller.
    var openNodeCallback = new js.Callback.many((event, data) {
      var openedNodeJs = data.rslt.obj;
      String id = openedNodeJs.attr('id');
      String text = openedNodeJs.attr('data');
      String type = openedNodeJs.attr('rel');
      TreeNode openedNode = new TreeNode(id, text, type);
      controller.add(openedNode);
    });
    
    // Bind the callback function to the event.
    js.context.jQuery(host).on('open_node.jstree', openNodeCallback);  
    
    return controller.stream;
  }
}

/**
 * Tree node that can be converted to json for jsTree.
 */
class TreeNode {
  static const String DOCUMENT_TYPE = 'document';
  static const String FOLDER_TYPE = 'folder';
  
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
  
  @override
  int get hashCode {
    int result = 17;
    result = 37 * result + id.hashCode;
    result = 37 * result + text.hashCode;
    result = 37 * result + type.hashCode;
    return result;
  }

  @override
  bool operator==(other) {
    if (identical(other, this)) return true;
    return (other is TreeNode
        && other.id == id 
        && other.text == text
        && other.type == type);
  }
}



