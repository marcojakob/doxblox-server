library doxblox;

import 'dart:html' hide Document;
import 'dart:async';

import 'package:web_ui/web_ui.dart';
import 'package:web_ui/watcher.dart' as watchers; 
import 'package:route/client.dart';
import 'package:bootjack/bootjack.dart';
import 'package:logging/logging.dart';
import 'package:logging_handlers/logging_handlers_shared.dart';

import 'ui/navigation/navigation_view.dart';
import 'ui/digest/digest_view.dart';
import 'ui/editor/editor_view.dart';
import 'ui/widget/split_panel.dart';

import 'model/model.dart';
import 'data/mock/mock_data.dart';

import 'data/data.dart' as data;
import 'events.dart' as events;
import 'urls.dart' as urls;

part 'layout_manager.dart';

final _log = new Logger("doxblox");

void main() {
  initLogging();
  initBootjackWidgets();
  
  // ////////////////////////////////////
  // TODO: Replace Mock with REST data access
  // /////////////////////////////////////
  data.init(new MockDataAccess());
  
  events.init(new events.EventBus());
  
  // Defer until the end of the event loop so that web components are loaded first.
  Timer.run(() {
    Element header = query("#header");
    var baseContainer = query("#base-container").xtag;
    // Initialize the controller.
    LayoutManager layoutManager = new LayoutManager(header, baseContainer);
    layoutManager.buildUi();
    
    initRouter();
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
}

/// Initializes the Bootstrap widgets that need some Dart code to work.
void initBootjackWidgets() {
  Dropdown.use();
  // Prevent default action for click events on dropdown-toggles.
  // Otherwise the link to "#" is followed by the browser.
  List<Element> dropdownToggles = queryAll(".dropdown-toggle");
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