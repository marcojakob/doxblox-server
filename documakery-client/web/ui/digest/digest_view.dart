library digest_view;

import 'dart:html';
import 'dart:async';
import 'package:web_ui/web_ui.dart';

import 'digest_cell.dart';

import '../../model/model.dart';
import '../../events.dart' as events;

class DigestView extends WebComponent {

  DivElement digestContainer;
  
  /**
   * Lifecycle method invoked whenever a component is added to the DOM.
   */
  inserted() {
    digestContainer = query('#digest-container');
  }
  
  /**
   * Sets and shows the digests for the [documentBlocks].
   */
  void setDigests(List<DocumentBlock> documentBlocks) {
    // Delete all previous digests
    digestContainer.children.clear();
    
    for (DocumentBlock block in documentBlocks) {
      DigestCell cell = new DigestCell(block)
        ..host = new Element.html('<div is="digest-cell" class="digest"></div>');
      
      var lifecycleCaller = new ComponentItem(cell)..create();
      digestContainer.children.add(cell.host);
      lifecycleCaller.insert();
      
      // Add click handler for digest selections
      cell.onClick.listen((_) => _handleDigestSelection(cell));
    }
  }
  
  void _handleDigestSelection(DigestCell digestCell) {
    deselectAllDigests();
    digestCell.host.classes.add('selected');
    // Fire event
    events.eventBus.fire(events.digestViewDocumentBlockSelected, digestCell.documentBlock);
  }
  
  void deselectAllDigests() {
    digestContainer.children.forEach((digest) => digest.classes.remove('selected'));
  }
}  
