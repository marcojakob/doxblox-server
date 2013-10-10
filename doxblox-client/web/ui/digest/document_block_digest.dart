library doxblox.document_block_digest;

import 'package:polymer/polymer.dart';

import '../../model/model.dart';

/**
 * A cell displaying a digest version of a document block.
 */
@CustomTag('doxblox-document-block-digest')
class DocumentBlockDigestElement extends PolymerElement {
  DocumentBlock docBlock;
  
  @observable
  bool selected;
  
  bool get applyAuthorStyles => true;
  
  created() {
    super.created();
    
    bindCssClass(this, 'selected', this, 'selected');
  }
}

