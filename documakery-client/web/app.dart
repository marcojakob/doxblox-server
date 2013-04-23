part of documakery;

/**
 * The main application controller.
 */
class AppController {
  
  final Element header;
  
  // Cannot import [SplitPanel] because it creates ambiguity. SplitPanel is
  // automatically added see [issue 237](https://github.com/dart-lang/web-ui/issues/237)
  final SplitPanel baseContainer;
  final NavigationView navigationView;
  final DigestView digestView;
  final EditorView editorView;
  
  final DataAccess _dataAccess;
  
  AppController(this.header, this.baseContainer, this.navigationView, 
      this.digestView, this.editorView, this._dataAccess);
  
  DataAccess get dataAccess => _dataAccess;
  
  /**
   * Builds the user interface.
   */
  void buildUi() {
    // Let the split panel fill in the entire screen
    window.onResize.listen(_onResized);
    _onResized(null); // call resize for the first time
    
    // Initialize the navigation view
    navigationView.refreshDocumentFolderTree(dataAccess.getDocumentFolders(),
        dataAccess.getDocuments());
    navigationView.documentFolderTree.onSelectNode.listen(handleDocumentSelectNode);
    navigationView.documentFolderTree.onOpenNode.listen(handleDocumentFolderOpenNode);
  }
  
  void handleDocumentSelectNode(var selectedNode) {
    Document selectedDoc = dataAccess.getDocumentById(selectedNode.attr('id'));
    
    List<QuestionBlock> blocks = 
        dataAccess.getQuestionBlocksByIds(selectedDoc.documentBlockIds);
    
    digestView.setDigests(blocks);
//    QuestionBlock block = new QuestionBlock()
//    ..id = selectedNode.attr('id')
//    ..title = 'Lorem ipsum dolor sit amet'
//    ..introduction = 'consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.'
//    ..libraryType = 'PUBLIC';
  }
  
  void handleDocumentFolderOpenNode(var selectedNode) {
    print('opened: ${selectedNode.attr("id")}');
//    DocumentFolder openedFolder = dataAccess.getDocumentFolderById(selectedNode.attr('id'));
//    List<Document> documents = dataAccess.getDocumentsByIds(openedFolder.documentIds);
//    for (Document document in documents) {
//      navigationView.documentFolderTree.addNodeToParentJsNode(selectedNode, document.id, document.name, "document");
//    }
  }
  
  /**
   * Handles window resize events.
   */
  void _onResized(Event event) {
    // Leave some space for the header
    int headerHeight = header.clientHeight;
    baseContainer.host.style.paddingTop = "${headerHeight}px";
    baseContainer.resize(window.innerWidth, window.innerHeight - headerHeight);
  }
}

