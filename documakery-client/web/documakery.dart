library documakery;

import 'dart:html' hide Document;
import 'dart:async';

// Cannot import [SplitPanel] because it creates ambiguity. SplitPanel is
// automatically added see [issue 237](https://github.com/dart-lang/web-ui/issues/237)
//import 'package:documakery/split_panel.dart';

import 'package:web_ui/web_ui.dart';
import 'package:web_ui/watcher.dart' as watchers; 
import 'package:event_bus/event_bus.dart';
import 'package:route/client.dart';
import 'package:bootjack/bootjack.dart';
import 'package:logging/logging.dart';
import 'package:logging_handlers/logging_handlers_shared.dart';

import 'ui/navigation/navigation_view.dart';
import 'ui/digest/digest_view.dart';
import 'ui/editor/editor_view.dart';

import 'model/model.dart';
import 'data/mock/mock_data.dart';

import 'data/data.dart' as data;
import 'events.dart' as events;
import 'urls.dart' as urls;

part 'layout_manager.dart';

void main() {
  initLogging();
  initBootjackWidgets();
  
  // ////////////////////////////////////
  // TODO: Replace Mock with REST data access
  // /////////////////////////////////////
  data.init(new MockDataAccess());
  
  events.init(new EventBus());
  
  
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
  // Default PrintHandler prints output to console.
  Logger.root.onRecord.listen(new PrintHandler());
  Logger.root.level = Level.FINEST;
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
    events.eventBus.fire(events.documentAndBlockSelect, [null, null]);  
  })
  ..addHandler(urls.document, (path) {
    String docId = urls.document.parse(path)[0];
    Document document = data.dataAccess.getDocumentById(docId);
    events.eventBus.fire(events.documentAndBlockSelect, [document, null]);  
  })
  ..addHandler(urls.documentBlock, (path) {
    List<String> groups = urls.documentBlock.parse(path);
    Document document = data.dataAccess.getDocumentById(groups[0]);
    DocumentBlock documentBlock = data.dataAccess.getDocumentBlockById(groups[1]);
    events.eventBus.fire(events.documentAndBlockSelect, [document, documentBlock]);  
  })
  ..listen(ignoreClick: true);
}