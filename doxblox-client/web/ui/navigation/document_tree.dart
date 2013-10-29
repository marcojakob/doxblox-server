library doxblox.document_tree;

import 'dart:html' show CustomEvent;
import 'dart:async';

import 'package:polymer/polymer.dart';
import 'package:logging/logging.dart';

// TODO: Change to package: when possible.
import 'packages/tree_view/tree_view.dart';

import '../../model/model.dart';
import '../../events.dart' as events;
import '../../urls.dart' as urls;

final _log = new Logger('doxblox.document_tree');

@CustomTag('doxblox-document-tree')
class DocumentTreeElement extends PolymerElement {
  
  /// The root of all tree items.
  @observable TreeItem root;
  
  bool get applyAuthorStyles => true;
  
  // The initially selected document.
  Document _initialSelection;
  
  DocumentTreeElement.created() : super.created() {
  }
  
  void ready() {
    super.ready();
    TreeViewElement tree = $['tree'];
    
    // Forward item selection as an event on event bus
    tree.onItemSelected.listen((CustomEvent event) {
      TreeItem item = event.detail;
      
      // Only react to document type selections.
      if (item is DocumentTreeItem) {
        _log.finest('Document item selected: ${item.name}');
        urls.router.gotoUrl(urls.document, [item.data.id], 'doxblox');
      }
    });
    
    // React to document selection events.
    events.eventBus.on(events.documentSelect).listen((Document doc) {
      if (doc != null) {
        _log.finest('Received document selection: doc.id=${doc.id}');
        if (root != null) {
          _selectDocument(doc);
        } else {
          // Tree is not ready yet, save for later.
          _initialSelection = doc;
        }
      }
    });
  }
  
  void _selectDocument(Document doc) {
    TreeViewElement tree = $['tree'];
    tree.selectWhere((TreeItem item) {
      return item is DocumentTreeItem && item.data == doc;
    });
  }
  
  /**
   * Builds the document tree with specified [folders] and [documents].
   */
  void buildTree(List<DocumentFolder> folders, List<Document> documents) {
    _log.finest('Building the document tree.');
    var builder = new DocumentTreeBuilder(folders, documents);
    root = builder.buildTree();
    
    if (_initialSelection != null) {
      new Future(() => _selectDocument(_initialSelection));
    }
  }
}

/**
 * A [TreeItem] for [Document]s.
 */
class DocumentTreeItem extends TreeItem<Document> {
  
  DocumentTreeItem(Document data, {TreeItem parent}) : 
    super(data, parent: parent, isLeaf: true);
  
  String get type => 'document'; 
  String get name => data.name; 
}

/**
 * A [TreeItem] for [DocumentFolder]s.
 */
class FolderTreeItem extends TreeItem<DocumentFolder> {
  
  FolderTreeItem(DocumentFolder data, {TreeItem parent: null, bool isLeaf: true}) : 
    super(data, parent: parent, isLeaf: isLeaf);
  
  String get type => 'folder'; 
  String get name => data.name; 
}

/**
 * Builds a document tree.
 */
class DocumentTreeBuilder {
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
  DocumentTreeBuilder(List<DocumentFolder> folders, 
      List<Document> documents) {
    _initChildFolderMapAndRoot(folders);
    _initDocumentMap(documents);
  }
  
  /**
   * Builds a tree with [TreeItem]s. Returns the root item.
   */
  TreeItem buildTree() {
    TreeItem rootItem = new FolderTreeItem(_rootFolder);
    _addChildFolders(rootItem, _rootFolder);
    
    return rootItem;
  }
  
  /**
   * Recursive function to add the child [folder]s to the [item].
   */
  void _addChildFolders(TreeItem item, DocumentFolder folder) {
    // Recursively add child folders.
    List childFolders = _childFolderMap[folder.id];
    if (childFolders != null) {
      for (DocumentFolder childFolder in childFolders) {
        TreeItem childItem = new FolderTreeItem(childFolder, parent: item);
        // Recursive call to add children of [childFolder]
        _addChildFolders(childItem, childFolder);
      }
    }
    
    // Add the child documents.
    _addChildDocuments(item, folder);
  }
  
  /**
   * Adds the documents of the [folder] to the [item].
   */
  void _addChildDocuments(TreeItem item, DocumentFolder folder) {
    for (String id in folder.documentIds) {
      Document doc = _documentMap[id];
      TreeItem docItem = new DocumentTreeItem(doc, parent: item);
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
