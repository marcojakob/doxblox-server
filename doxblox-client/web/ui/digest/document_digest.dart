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
  @observable
  Document doc;
  
  @observable
  List<DocumentBlock> docBlocks = [];
  
  /// Current selected [DocumentBlock].
  @observable
  DocumentBlock selectedDocBlock;
  
  bool get applyAuthorStyles => true;
  
  created() {
    super.created();
    
//    // Create observer for doc to get the new blocks when it changes.
//    new PathObserver(doc, '')
//      ..bindSync((Document newDoc) {
//        if (newDoc == null) {
//          _log.finest('doc changed: null');
//          docBlocks = [];
//        } else {
//          _log.finest('doc changed: doc.id=${newDoc.id}');
//          dataAccess.documentBlocks.getAllByIds(newDoc.documentBlockIds).then((List<DocumentBlock> blocks) {
//            docBlocks = blocks;
//            notifyProperty(this, const Symbol('docBlocks'));
//          });
//        }
//      });
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
        docBlocks = blocks;
        notifyProperty(this, #docBlocks);
      });
    }
  }
  
  /**
   * Called when a digest is selected.
   */
  void _handleDigestSelection(Event e, var detail, Node target) {
    this.selectedDocBlock = (target as DocumentBlockDigestElement).docBlock;
    // Fire url change
    urls.router.gotoUrl(urls.documentBlock, 
        [doc.id, selectedDocBlock.id], 'doxblox');
  }
}  
