library doxblox.document_digest;

import 'package:polymer/polymer.dart';
import 'dart:html' hide Document;
import 'dart:async';
import 'package:logging/logging.dart';

import 'document_block_digest.dart';

import '../../model/model.dart';
import '../../data/data.dart';
import '../../urls.dart' as urls;

final _log = new Logger("doxblox.document_digest");

/**
 * View to display a [Document] with it's [DocumentBlock]s as digests.
 */
@CustomTag('doxblox-document-digest')
class DocumentDigestElement extends PolymerElement {
  /// The [Document] displayed in this view. Called [doc] to not interfere with
  /// getters from [PolymerElement].
  @published
  Document doc;
  
  /// Current selected [DocumentBlock].
  @published
  DocumentBlock selectedDocBlock;
  
  @observable
  List<DocumentBlock> docBlocks = [];
  
  bool get applyAuthorStyles => true;
  
  DocumentDigestElement.created() : super.created() {
  }
  
  /**
   * Is automatically called when the [doc] attribute changed.
   */
  void docChanged(Document oldDoc) {
    if (doc == null) {
      _log.finest('doc changed: null');
      docBlocks = [];
    } else {
      _log.finest('doc changed: doc.id=${doc.id}');
      dataAccess.documentBlocks.getAllByIds(doc.documentBlockIds).then((List<DocumentBlock> blocks) {
        var oldDocBlocks = docBlocks;
        docBlocks = blocks;
        notifyPropertyChange(#docBlocks, oldDocBlocks, docBlocks);
      });
    }
  }
  
  /**
   * Called when a digest is selected.
   */
  void handleDigestSelection(Event event, var detail, Node target) {
    this.selectedDocBlock = (target as DocumentBlockDigestElement).docBlock;
    // Fire url change
    urls.router.gotoUrl(urls.documentBlock, 
        [doc.id, selectedDocBlock.id], 'doxblox');
  }
}  
