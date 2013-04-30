library navigation_view;

import 'dart:html' hide Document;
import 'dart:async';
import 'package:web_ui/web_ui.dart';

// Cannot import [TreeView] because it creates ambiguity. TreeView 
// is automatically added through the reference in navigation_view.html.
// See also [issue 237](https://github.com/dart-lang/web-ui/issues/237)
//import 'package:documakery/tree_view.dart';

import '../../model/model.dart';
import '../../events.dart' as events;

class NavigationView extends WebComponent {
  /// The Tree with folders and documents
  TreeView documentFolderTree;
  /// A map with document id as `key` and the document as `value`.
  Map<String, Document> documentMap;
  
  /**
   * Lifecycle method invoked whenever a component is added to the DOM.
   */
  inserted() {
    documentFolderTree = query('#document-tree').xtag;
    
    // Forward node selection as an event on event bus
    documentFolderTree.onSelectNode().listen((node) {
      events.eventBus.fire(events.navigationViewDocumentSelected, documentMap[node.attr('id')]);
    });
  }
  
  /**
   * Refreshes the tree.
   */
  void refreshDocumentFolderTree(List<DocumentFolder> folders, 
                                 List<Document> documents) {
    if (folders == null || folders.isEmpty) {
      documentFolderTree.host = null;
      return;
    }
    var builder = new DocumentFolderTreeBuilder(folders, documents);
    TreeNode rootNode = builder.buildJsonTree();
    documentFolderTree.initTree(rootNode);
    
    // Save the documentMap for later.
    documentMap = builder._documentMap;
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
    TreeNode rootNode = new TreeNode(_rootFolder.id, _rootFolder.name, 'folder');
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
        TreeNode childNode = new TreeNode(childFolder.id, childFolder.name, 'folder');
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
      TreeNode docNode = new TreeNode(id, doc.name, 'document');
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



