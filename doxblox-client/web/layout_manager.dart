part of doxblox;

/**
 * The main layout manager.
 */
class LayoutManager {
  
  final Element header;
  
  final SplitPanel baseContainer;
  
  LayoutManager(this.header, this.baseContainer);
  
  /**
   * Builds the user interface.
   */
  void buildUi() {
    // Let the split panel fill in the entire screen
    window.onResize.listen(_onResized);
    _onResized(null); // call resize for the first time
  }
  
  /**
   * Handles window resize events.
   */
  void _onResized(Event event) {
    // Leave some space for the header
    int headerHeight = header.clientHeight;
    baseContainer.style.paddingTop = "${headerHeight}px";
    baseContainer.resize(window.innerWidth, window.innerHeight - headerHeight);
  }
}

