library digest_cell;

import 'package:web_ui/web_ui.dart';
import 'package:meta/meta.dart';

import '../../model/model.dart';

class DigestCell extends WebComponent {
  DocumentBlock documentBlock;
  
  @observable
  bool selected;
  
  /**
   * Invoked when component is added to the DOM.
   */
  @override
  inserted() {
    bindCssClasses(this, () => selected ? 'digest-cell_selected' : null);
  }
}

