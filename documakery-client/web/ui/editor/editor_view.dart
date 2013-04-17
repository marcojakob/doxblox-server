library editor_view;

import 'dart:html';
import 'package:web_ui/web_ui.dart';

import '../../model/document.dart';

class EditorView extends WebComponent {
  
  /**
   * Lifecycle method invoked whenever a component is added to the DOM.
   */
  inserted() {
    refreshView();
  }
  
  /**
   * Refreshes the view.
   */
  void refreshView() {
  }
}
