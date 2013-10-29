library doxblox.navigation_view;

import 'dart:async';

import 'package:polymer/polymer.dart';
import 'package:logging/logging.dart';

import 'document_tree.dart';

import '../../model/model.dart';
import '../../data/data.dart';

final _log = new Logger('doxblox.navigation_view');

@CustomTag('doxblox-navigation-view')
class NavigationViewElement extends PolymerElement {
  
  bool get applyAuthorStyles => true;
  
  NavigationViewElement.created() : super.created() {
  }
  
  void ready() {
    super.ready();
    
    initDocumentTree();
  }
  
  /**
   * Initializes the document tree.
   */
  void initDocumentTree() {
    Future<List<DocumentFolder>> foldersFuture = dataAccess.documentFolders.getAll();
    Future<List<Document>> docsFuture = dataAccess.documents.getAll();
    
    Future.wait([foldersFuture, docsFuture]).then((List result) {
      // Test if there are any folders.
      if (result[0] == null || result[0].isEmpty) {
        return;
      }
      
      DocumentTreeElement tree = $['document-tree'];
      tree.buildTree(result[0], result[1]);
    });
  }
}