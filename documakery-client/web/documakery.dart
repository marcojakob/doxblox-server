library documakery;

import 'dart:html';
import 'package:web_ui/web_ui.dart';
import 'dart:async';

import 'ui/split_panel.dart';
import 'ui/navigation/navigation_view.dart';
import 'ui/digest/digest_view.dart';
import 'ui/editor/editor_view.dart';

import 'model/models.dart';
import 'data/data_access.dart';

part 'app.dart';


/// Holds the running app
AppController _appController;
AppController get appController => _appController;


void main() {
  // defer until the end of the event loop so that web components are loaded first
  Timer.run(() {
    Element header = query("#header");
    Element baseContainerElement = query("#split-container");
    
    NavigationView navigationView = query('#navigation-view').xtag;
    DigestView digestView = query('#digest-view').xtag;
    EditorView editorView = query('#editor-view').xtag;
    
    // TODO: Replace with REST data access
    DataAccess dataAccess = new MockDataAccess();
    
    // Initialize the controller.
    _appController = new AppController(header, baseContainerElement, 
        navigationView, digestView, editorView, dataAccess);
    
    appController.buildUi();
  });
}