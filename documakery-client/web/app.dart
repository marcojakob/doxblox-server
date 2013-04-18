part of documakery;

/**
 * The main application controller.
 */
class AppController {
  
  final Element header;
  final Element baseContainerElement;
  SplitPanelContainer baseContainer;
  final NavigationView navigationView;
  final DigestView digestView;
  final EditorView editorView;
  
  final DataAccess _dataAccess;
  
  AppController(this.header, this.baseContainerElement, this.navigationView, 
      this.digestView, this.editorView, this._dataAccess);
  
  DataAccess get dataAccess => _dataAccess;
  
  /**
   * Builds the user interface.
   */
  void buildUi() {
    // Create the split panels and add them to the split panel container.
    List<SplitPanel> panels = new List();
    panels.add(new SplitPanel(navigationView.host, 0.2));
    panels.add(new SplitPanel(digestView.host, 0.2));
    panels.add(new SplitPanel(editorView.host, 0.6));
    
    baseContainer = new SplitPanelContainer(baseContainerElement, 
        panels, stackedVertical: false);
    
    // Let the split panel fill in the entire screen
    window.onResize.listen(_onResized);
    _onResized(null); // call resize for the first time
    
    // Initialize the navigation view
    navigationView.refreshDocumentFolderTree(dataAccess.documentFolders);
    navigationView.documentFolderTree.onNodeSelect.listen((id) => print(id));
  }

  /**
   * Handles window resize events.
   */
  void _onResized(Event event) {
    // Leave some space for the header
    int headerHeight = header.clientHeight;
    baseContainerElement.style.paddingTop = "${headerHeight}px";
    baseContainer.resize(window.innerWidth, window.innerHeight - headerHeight);
  }
}

