library doxblox.digest_view;

import 'package:polymer/polymer.dart';
import 'dart:html' hide Document;
import 'dart:async';
import 'package:logging/logging.dart';


import '../../model/model.dart';
import '../../events.dart' as events;

final _log = new Logger("doxblox.digest_view");

/**
 * View displaying a list of [DocumentBlock]s from a library or a  
 */
@CustomTag('doxblox-digest-view')
class DigestViewElement extends PolymerElement {
  DivElement digestContainer;
  
  /// The [Document] displayed in this view. [doc] is null if a library is 
  /// displayed. Called [doc] to not interfere with getters from [WebComponent].
  @observable
  Document doc;
  
  /// [DocumentBlock] that is currently selected.
  @observable
  DocumentBlock selectedDocumentBlock;
  
  bool get applyAuthorStyles => true;
  
  DigestViewElement.created() : super.created();
  
  /**
   * Invoked when component is added to the DOM.
   */
  enteredView() {
    super.enteredView();
    
    digestContainer = querySelector('#digest-container');
    
    // Initialize listeners
    events.eventBus.on(events.documentSelect).listen((Document doc) {
      this.doc = doc;
    });
    
    events.eventBus.on(events.documentBlockSelect).listen((DocumentBlock block) {
      this.selectedDocumentBlock = block;
    });
  }
}  
