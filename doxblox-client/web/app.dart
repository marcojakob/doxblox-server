library doxblox;

import 'dart:html' hide Document;
import 'dart:async';

import 'package:polymer/polymer.dart';
import 'package:route/client.dart';
import 'package:bootjack/bootjack.dart';
import 'package:logging/logging.dart';
import 'package:logging_handlers/logging_handlers_shared.dart';

import 'ui/widget/split_panel.dart';

import 'model/model.dart';
import 'data/mock/mock_data.dart';

import 'data/data.dart' as data;
import 'events.dart' as events;
import 'urls.dart' as urls;

final _log = new Logger("doxblox");

@CustomTag('doxblox-app')
class DoxbloxApp extends PolymerElement {
  
  bool get applyAuthorStyles => true;
  
  DoxbloxApp() {
    initLogging();
    
    // ////////////////////////////////////
    // TODO: Replace Mock with REST data access
    // /////////////////////////////////////
    data.init(new MockDataAccess());
    
    events.init(new events.EventBus());
  }
  
  void created() {
    super.created();  
    
    initBootjackWidgets();
  }
  
  void inserted() {
    super.inserted();
    
    initLayout();
    
    initRouter();
  }
  
  void initLayout() {
    Element header = $['header'];
    SplitPanelElement baseContainer = $['base-container'].xtag;
    
    // Initial layout with panel size ratios.
    int headerHeight = header.clientHeight;
    baseContainer.style.paddingTop = "${headerHeight}px";
    baseContainer.resizeWithRatios(window.innerWidth, window.innerHeight - headerHeight);
    
    // Handle window resize events.
    window.onResize.listen((_) {
      // Must get header height again since it might have changed.
      headerHeight = header.clientHeight;
      baseContainer.style.paddingTop = "${headerHeight}px";
      baseContainer.resize(window.innerWidth, window.innerHeight - headerHeight);
    });
  }
  
  /// Initializes the logging handler and log level.
  void initLogging() {
    hierarchicalLoggingEnabled = true;
    
    // Default PrintHandler prints output to console.
    Logger.root.onRecord.listen(new PrintHandler().call);
    
    // Root logger level.
    Logger.root.level = Level.INFO;
    
    // Doxblox logger level (affects all loggers starting with 'doxblox.').
    new Logger('doxblox')..level = Level.ALL;
    
    _log.info('logging initialized');
  }
  
  /// Initializes the Bootstrap widgets that need some Dart code to work.
  void initBootjackWidgets() {
    _log.info('initializing bootjack widgets');
    
    Dropdown.use();
    // Prevent default action for click events on dropdown-toggles.
    // Otherwise the link to "#" is followed by the browser.
    var root = getShadowRoot('doxblox-app');
    List<Element> dropdownToggles = root.queryAll('.dropdown-toggle');
    for (Element toggle in dropdownToggles) {
      toggle.onClick.listen((Event e) => e.preventDefault());
    }
  }
  
  void initRouter() {
    // Start listening for window history events.
    urls.init(new Router(useFragment: true));
    urls.router
      ..addHandler(urls.home, (_) {
        events.eventBus.fire(events.documentSelect, null);
        events.eventBus.fire(events.documentBlockSelect, null);  
      })
      ..addHandler(urls.document, (path) {
        _log.finest('matching urls.document path: $path');
        String docId = urls.document.parse(path)[0];
        Future<Document> docFuture = data.dataAccess.documents.getById(docId);
        docFuture.then((Document doc) => 
            events.eventBus.fire(events.documentSelect, doc));  
      })
      ..addHandler(urls.documentBlock, (path) {
        _log.finest('matching urls.documentBlock path: $path');
        List<String> groups = urls.documentBlock.parse(path);
        Future<Document> docFuture = data.dataAccess.documents.getById(groups[0]);
        Future<DocumentBlock> docBlockFuture = data.dataAccess.documentBlocks.getById(groups[1]);
        Future.wait([docFuture, docBlockFuture]).then((List result) {
          events.eventBus.fire(events.documentSelect, result[0]);  
          events.eventBus.fire(events.documentBlockSelect, result[1]);  
        });
      })
      ..listen(ignoreClick: true);
  }
}
