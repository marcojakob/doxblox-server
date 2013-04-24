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
  
  /// The data access
  DataAccess get dataAccess => _dataAccess;
  
  /**
   * Builds the user interface.
   */
  void buildUi() {
    // Let the split panel fill in the entire screen
    window.onResize.listen(_onResized);
    _onResized(null); // call resize for the first time
    
    // Initialize the navigation view and its selection listener
    navigationView.refreshDocumentFolderTree(dataAccess.getDocumentFolders(),
        dataAccess.getDocuments());
    navigationView.documentFolderTree.onSelectNode().listen(handleDocumentSelectNode);
    
    // Initialize digest view and its selection listener
    digestView.onDigestSelection().listen((cell) => print(cell));
    ////////////////// TODO: display questions in editor
    
  }
  
  void handleDocumentSelectNode(var selectedNode) {
    Document selectedDoc = dataAccess.getDocumentById(selectedNode.attr('id'));
    
    // Get the question blocks of the selected document
    List<DocumentBlock> blocks = 
        dataAccess.getDocumentBlocksByIds(selectedDoc.documentBlockIds);
    
    digestView.setDigests(blocks);
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

