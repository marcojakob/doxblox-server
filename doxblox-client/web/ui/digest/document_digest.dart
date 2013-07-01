library doxblox.document_digest;

import 'dart:html' hide Document;
import 'dart:async';
import 'package:logging/logging.dart';
import 'package:web_ui/web_ui.dart';

import 'document_block_digest.dart';

import '../../model/model.dart';
import '../../data/data.dart';
import '../../urls.dart' as urls;

final _log = new Logger("doxblox.document_digest");

/**
 * View to display a [Document] with it's [DocumentBlock]s as digests.
 */
class DocumentDigest extends WebComponent {
  /// The [Document] displayed in this view. Called [doc] to not interfere with
  /// getters from [WebComponent].
  @observable
  Document doc;
  
  @observable
  List<DocumentBlock> docBlocks = [];
  
  /// Current selected [DocumentBlock].
  @observable
  DocumentBlock selectedDocBlock;
  
  created() {
    // Create observer for doc to get the new blocks when it changes.
    observe(() => doc, (_) {
      if (doc == null) {
        _log.finest('doc changed: null');
        docBlocks = [];
      } else {
        _log.finest('doc changed: doc.id=${doc.id}');
        dataAccess.documentBlocks.getAllByIds(doc.documentBlockIds).then(
            (List<DocumentBlock> blocks) => docBlocks = blocks);
      }
    });
  }
  
  /**
   * Called when a digest is selected.
   */
  void _handleDigestSelection(DocumentBlock docBlock) {
    this.selectedDocBlock = docBlock;
    // Fire url change
    urls.router.gotoUrl(urls.documentBlock, 
        [doc.id, docBlock.id], 'doxblox');
  }
}  
