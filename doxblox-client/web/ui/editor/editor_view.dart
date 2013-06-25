library editor_view;

import 'dart:html';
import 'dart:async';
import 'package:web_ui/web_ui.dart';

import '../../model/model.dart';
import '../../data/data.dart' as data;
import '../../events.dart' as events;

class EditorView extends WebComponent {
  StreamSubscription _documentSelectionSubscription;
  
  @observable
  QuestionBlock questionBlock;
  
  /**
   * Invoked when component is added to the DOM.
   */
  inserted() {
    // Subscribe to document block selection changes
    _documentSelectionSubscription = 
        events.eventBus.on(events.documentAndBlockSelect).listen((List documentAndBlock) {
      if (documentAndBlock[1] is QuestionBlock) {
        questionBlock = documentAndBlock[1];
      } else {
        questionBlock = null;
      }
    });
  }
  
  /**
   * Invoked when component is removed from the DOM.
   */
  removed() {
    // Cancel subscription.
    if (_documentSelectionSubscription != null) {
      _documentSelectionSubscription.cancel();
    }
  }
  
}
