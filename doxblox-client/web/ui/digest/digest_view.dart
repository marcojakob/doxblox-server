library doxblox.digest_view;

import 'dart:html' hide Document;
import 'dart:async';
import 'package:logging/logging.dart';
import 'package:web_ui/web_ui.dart';

import 'document_block_digest.dart';

import '../../model/model.dart';
import '../../data/data.dart' as data;
import '../../events.dart' as events;
import '../../urls.dart' as urls;

final _log = new Logger("doxblox.digest_view");

/**
 * View displaying a list of [DocumentBlock]s from a library or a  
 */
class DigestView extends WebComponent {
  DivElement digestContainer;
  
  /// The [Document] displayed in this view. [doc] is null if a library is 
  /// displayed. Called [doc] to not interfere with getters from [WebComponent].
  @observable
  Document doc;
  
  /// [DocumentBlock] that is currently selected.
  @observable
  DocumentBlock selectedDocumentBlock;
  
  /**
   * Invoked when component is added to the DOM.
   */
  inserted() {
    digestContainer = query('#digest-container');
    
    // Initialize listeners
    events.eventBus.on(events.documentSelect).listen((Document doc) {
      _log.finest('documentSelected received: doc.id=${doc.id}');
      this.doc = doc;
    });
    
    events.eventBus.on(events.documentBlockSelect).listen((DocumentBlock block) {
      _log.finest('documentBlockSelected received: block.id=${block.id}');
      this.selectedDocumentBlock = block;
    });
  }
}  
