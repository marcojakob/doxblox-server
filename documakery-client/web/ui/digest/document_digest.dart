library digest_view;

import 'dart:html' hide Document;
import 'dart:async';
import 'package:web_ui/web_ui.dart';

import 'document_block_digest.dart';

import '../../model/model.dart';
import '../../data/data.dart' as data;
import '../../urls.dart' as urls;

/**
 * View to display a [Document] with it's [DocumentBlock]s as digests.
 */
class DocumentDigest extends WebComponent {
  /// The [Document] displayed in this view. Called [doc] to not interfere with
  /// getters from [WebComponent].
  @observable
  Document doc;
  
  /// Current selected [DocumentBlock].
  @observable
  DocumentBlock selectedDocumentBlock;
  
  /**
   * Invoked when component is added to the DOM.
   */
  inserted() {
  }
  
  /**
   * Returns the list of [DocumentBlock]s of the [doc] in this view.
   */
  List<DocumentBlock> _getDocumentBlocks() {
    if (doc == null || doc.documentBlockIds == null) {
      return [];
    } else {
      return data.dataAccess.getDocumentBlocksByIds(doc.documentBlockIds);
    }
  }
  
  /**
   * Called when a digest is selected.
   */
  void _handleDigestSelection(DocumentBlock documentBlock) {
    this.selectedDocumentBlock = documentBlock;
    // Fire url change
    urls.router.gotoUrl(urls.documentBlock, 
        [doc.id, documentBlock.id], 'documakery');
  }
}  
