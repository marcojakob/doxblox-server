library doxblox.navigation_view;

import 'package:polymer/polymer.dart';
import 'dart:async';
import 'package:logging/logging.dart';

import 'tree_view.dart';
import '../../model/model.dart';
import '../../data/data.dart';
import '../../events.dart' as events;
import '../../urls.dart' as urls;

final _log = new Logger("doxblox.navigation_view");

@CustomTag('doxblox-navigation-view')
class NavigationViewElement extends PolymerElement {
  
  @observable TreeNode documentTreeRootNode;
  
  bool get applyAuthorStyles => true;
  
  created() {
    super.created();
    
    var root = getShadowRoot('doxblox-navigation-view');
    TreeViewElement _documentFolderTree = root.query('#document-tree').xtag;
    
    // Forward node selection as an event on event bus
    _documentFolderTree.onSelectNode().listen((TreeNode node) {
      // Only react to [TreeNode.DOCOUMENT_TYPE] selections.
      if (node.type == TreeNode.DOCUMENT_TYPE) {
        _log.finest('document node selected: node.id=${node.id}');
        urls.router.gotoUrl(urls.document, [node.id], 'doxblox');
      }
    });
    
    createDocumentFolderTree();
    
    // Init listener.
    events.eventBus.on(events.documentSelect).listen((Document doc) {
      if (doc != null) {
        _log.finest('received document selection: doc.id=${doc.id}');
        _documentFolderTree.selectNode(doc.id, TreeNode.DOCUMENT_TYPE);
      }
    });
  }
  
  /**
   * Refreshes the tree.
   */
  void createDocumentFolderTree() {
    Future<List<DocumentFolder>> foldersFuture = dataAccess.documentFolders.getAll();
    Future<List<Document>> docsFuture = dataAccess.documents.getAll();
    
    Future.wait([foldersFuture, docsFuture]).then((List result) {
      // Test if there are any folders.
      if (result[0] == null || result[0].isEmpty) {
        return;
      }
      
      var builder = new DocumentFolderTreeBuilder(result[0], result[1]);
      documentTreeRootNode = builder.buildJsonTree();
    });
    
  }
}


/**
 * Builds a document folder tree.
 */
class DocumentFolderTreeBuilder {
  /// Map with document id as key and the document as value.
  Map<String, Document> _documentMap = new Map();
  /// Map with folder id of parent as key and the list of child folders as value.
  Map<String, List<DocumentFolder>> _childFolderMap = new Map();
  /// The root folder
  DocumentFolder _rootFolder;
  
  /**
   * Constructs a tree builder for the specified [folders] and [documents]. 
   * The [folders] must have exactly one root folder, i.e. a folter with 
   * parentId == null.
   */
  DocumentFolderTreeBuilder(List<DocumentFolder> folders, 
      List<Document> documents) {
    _initChildFolderMapAndRoot(folders);
    _initDocumentMap(documents);
  }
  
  /**
   * Builds a tree with [TreeNode]s. Returns the root node.
   */
  TreeNode buildJsonTree() {
    TreeNode rootNode = new TreeNode(_rootFolder.id, _rootFolder.name, TreeNode.FOLDER_TYPE);
    _addChildFolders(rootNode, _rootFolder);
    
    return rootNode;
  }
  
  /**
   * Recursive function to add the child [folder]s to the [node].
   */
  void _addChildFolders(TreeNode node, DocumentFolder folder) {
    // Recursively add child folders.
    List childFolders = _childFolderMap[folder.id];
    if (childFolders != null) {
      for (DocumentFolder childFolder in childFolders) {
        TreeNode childNode = new TreeNode(childFolder.id, childFolder.name, TreeNode.FOLDER_TYPE);
        node.children.add(childNode);
        // Recursive call to add children of [childFolder]
        _addChildFolders(childNode, childFolder);
      }
    }
    
    // Add the child documents.
    _addChildDocuments(node, folder);
  }
  
  /**
   * Adds the documents of the [folder] to the [node].
   */
  void _addChildDocuments(TreeNode node, DocumentFolder folder) {
    for (String id in folder.documentIds) {
      Document doc = _documentMap[id];
      TreeNode docNode = new TreeNode(id, doc.name, TreeNode.DOCUMENT_TYPE);
      node.children.add(docNode);
    }
  }
  
  /**
   * Populates the child folder map and identifies the root folder.
   */
  void _initChildFolderMapAndRoot(List<DocumentFolder> folders) {
    for (DocumentFolder folder in folders) {
      if (folder.parentId == null) {
        _rootFolder = folder;
      } else {
        // Add current folder as child of its parent.
        List<DocumentFolder> childFolders = 
            _childFolderMap.putIfAbsent(folder.parentId, () => new List());
        childFolders.add(folder);
      }
    }      
  }
  
  /**
   * Populates the document map.
   */
  void _initDocumentMap(List<Document> documents) {
    for (Document document in documents) {
      _documentMap[document.id] = document;
    }
  }
}



