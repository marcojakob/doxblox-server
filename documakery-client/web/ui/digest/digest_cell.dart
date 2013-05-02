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
    // Create observer to watch for selection changes.
    observe(() => selected, (ChangeNotification e) {
      _refreshSelection();
    });
    
    _refreshSelection();
  }
  
  void _refreshSelection() {
    if (selected) {
      host.style.backgroundColor = '#789e35';
      host.style.color = 'white';
    } else {
      host.style.backgroundColor = null;
      host.style.color = null;
    }
  }
}

