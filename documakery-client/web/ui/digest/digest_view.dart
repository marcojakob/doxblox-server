library digest_view;

import 'dart:html';
import 'package:web_ui/web_ui.dart';

import 'digest_cell.dart';

import '../../model/model.dart';

class DigestView extends WebComponent {

  DivElement digestContainer;
  
  /**
   * Lifecycle method invoked whenever a component is added to the DOM.
   */
  inserted() {
    digestContainer = query('#digest-container');
  }
  
  /**
   * Sets and shows the digests.
   */
  void setDigests(List<QuestionBlock> questionBlocks) {
    // Delete all previous digests
    digestContainer.children.clear();
    
    for (QuestionBlock block in questionBlocks) {
      DigestCell cell = new DigestCell(block)
        ..host = new Element.html('<div is="digest-cell" class="digest"></div>');
      var lifecycleCaller = new ComponentItem(cell)..create();
      digestContainer.children.add(cell.host);
      lifecycleCaller.insert();
    }
  }
}  
