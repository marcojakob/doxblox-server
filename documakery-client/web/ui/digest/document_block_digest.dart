library document_block_digest;

import 'package:web_ui/web_ui.dart';
import 'package:meta/meta.dart';

import '../../model/model.dart';

/**
 * A cell displaying a digest version of a document block.
 */
class DocumentBlockDigest extends WebComponent {
  DocumentBlock documentBlock;
  
  @observable
  bool selected;
  
  /**
   * Invoked when component is added to the DOM.
   */
  @override
  inserted() {
    bindCssClasses(this, () => selected ? 'selected' : null);
  }
}

