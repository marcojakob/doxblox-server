library digest_view;

import 'dart:html' hide Document;
import 'dart:async';
import 'package:web_ui/web_ui.dart';

import 'document_block_digest.dart';

import '../../model/model.dart';
import '../../data/data.dart' as data;
import '../../events.dart' as events;
import '../../urls.dart' as urls;

class DigestView extends WebComponent {
  DivElement digestContainer;
  
  /// The [Document] displayed in this view. Called [doc] to not interfere with
  /// getters from [WebComponent].
  @observable
  Document doc;
  
  @observable
  DocumentBlock selectedDocumentBlock;
  
  /**
   * Invoked when component is added to the DOM.
   */
  inserted() {
    digestContainer = query('#digest-container');
    
    // Initialize listeners
    events.eventBus.on(events.documentAndBlockSelect).listen((List documentAndBlock) {
      this.doc = documentAndBlock[0];
      this.selectedDocumentBlock = documentAndBlock[1];
    });
  }
}  
