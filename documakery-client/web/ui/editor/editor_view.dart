library editor_view;

import 'dart:html';
import 'dart:async';
import 'package:web_ui/web_ui.dart';

import '../../model/model.dart';
import '../../events.dart' as events;

class EditorView extends WebComponent {
  
  @observable
  List<Question> questions = new List();
  
  StreamSubscription documentSelectionSubscription;
  
  /**
   * Lifecycle method invoked whenever a component is added to the DOM.
   */
  inserted() {
    // Subscribe to document block selection changes
    documentSelectionSubscription = events.eventBus.on(events.digestViewDocumentBlockSelected).listen((DocumentBlock block) {
      if (block is QuestionBlock) {
        questions = block.questions;
      }
    });
  }
  
  /**
   * Lifecycle method invoked whenever a component is removed from the DOM.
   */
  removed() {
    // Cancel subscription.
    if (documentSelectionSubscription != null) {
      documentSelectionSubscription.cancel();
    }
  }
  
}
