library navigation_view;

import 'dart:html';
import 'dart:async';
import 'package:web_ui/web_ui.dart';

import 'tree_view.dart';
import '../../model/models.dart';

class NavigationView extends WebComponent {
  Element documentFolderTreeElement;
  TreeView documentFolderTree;
  
  /**
   * Lifecycle method invoked whenever a component is added to the DOM.
   */
  inserted() {
    documentFolderTreeElement = query('#document-tree');
    documentFolderTree = documentFolderTreeElement.xtag;
  }
  
  /**
   * Refreshes the view.
   */
  void refreshDocumentFolderTree(List<DocumentFolder> folders) {
    if (folders == null || folders.isEmpty) {
      documentFolderTreeElement = null;
      return;
    }
    var builder = new DocumentFolderTreeBuilder(folders);
    builder.buildHtmlTree(documentFolderTreeElement);
    documentFolderTree.initTree();
  }
}


/**
 * Builds a document folder tree.
 */
class DocumentFolderTreeBuilder {
  /// Map with folder id as key and the list of child folders as value.
  Map<String, List<DocumentFolder>> _childFolderMap = new Map();
  
  /// The root folder
  DocumentFolder _rootFolder;
  
  /**
   * Constructs a tree builder for the specified [folders]. The [folders] must 
   * have exactly one root folder, i.e. a folter with parentId == null.
   */
  DocumentFolderTreeBuilder(List<DocumentFolder> folders) {
    _initChildFolderMapAndRoot(folders);
  }
  
  /**
   * Builds a tree with a hierarchical html list and adds it 
   * as a child to the [hostElement].
   */
  void buildHtmlTree(Element hostElement) {
    // Create a new <ul> element for the root.
    var rootUListElement = new UListElement();
    hostElement.children.add(rootUListElement);
    _addChildFolder(rootUListElement, _rootFolder);
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
   * Recursive function to add the [folder] and its children to the [parentElement].
   */
  void _addChildFolder(UListElement parentElement, DocumentFolder folder) {
    // Add a <li> element for the folder and add it to the [parentElement].
    var liElement = _createListItem(folder);
    parentElement.children.add(liElement);
    
    // Recursively add child folders.
    List children = _childFolderMap[folder.id];
    if (children != null) {
      // Create a new <ul> element for the children.
      var childUListElement = new UListElement();
      liElement.children.add(childUListElement);
      
      for (DocumentFolder child in children) {
        _addChildFolder(childUListElement, child);
      }
    }
  }
  
  /**
   * Creates a list item from the specified [folder].
   */
  Element _createListItem(DocumentFolder folder) {
    return new Element.html('''
        <li id="${folder.id}">
            <a href="#">${folder.name}</a>
        </li>''');
  }
}



