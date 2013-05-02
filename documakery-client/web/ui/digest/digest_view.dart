library digest_view;

import 'dart:html' hide Document;
import 'dart:async';
import 'package:web_ui/web_ui.dart';

import 'digest_cell.dart';

import '../../model/model.dart';
import '../../data/data.dart' as data;
import '../../events.dart' as events;
import '../../urls.dart' as urls;

class DigestView extends WebComponent {
  DivElement digestContainer;
  
  /// The [Document] displayed in this view.
  @observable
  Document document;
  
  /// Current selected digest.
  @observable
  DocumentBlock selectedDocumentBlock;
  
  /**
   * Invoked when component is added to the DOM.
   */
  inserted() {
    digestContainer = query('#digest-container');
    
    // Initialize listeners
    events.eventBus.on(events.documentAndBlockSelect).listen((List documentAndBlock) {
      this.document = documentAndBlock[0];
      this.selectedDocumentBlock = documentAndBlock[1];
    });
  }
  
  /**
   * Returns the list of [DocumentBlock]s of the [document] in this view.
   */
  List<DocumentBlock> _getDocumentBlocks() {
    if (document == null || document.documentBlockIds == null) {
      return [];
    } else {
      return data.dataAccess.getDocumentBlocksByIds(document.documentBlockIds);
    }
  }
  
  /**
   * Called when a digest is selected.
   */
  void _handleDigestSelection(DocumentBlock documentBlock) {
    this.selectedDocumentBlock = documentBlock;
    // Fire url change
    urls.router.gotoUrl(urls.documentBlock, 
        [document.id, documentBlock.id], 'documakery');
  }
}  
