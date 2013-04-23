library documakery;

import 'dart:html' hide Document;
import 'dart:async';
import 'package:web_ui/web_ui.dart';

import 'ui/navigation/navigation_view.dart';
import 'ui/digest/digest_view.dart';
import 'ui/editor/editor_view.dart';

import 'model/model.dart';
import 'data/data.dart';
import 'data/mock/mock_data.dart';

part 'app.dart';


/// Holds the running app
AppController _appController;
AppController get appController => _appController;


void main() {
  // defer until the end of the event loop so that web components are loaded first
  Timer.run(() {
    Element header = query("#header");
    
    var baseContainer = query("#base-container").xtag;
    
    DigestView digestView = query('#digest-view').xtag;
    NavigationView navigationView = query('#navigation-view').xtag;
    EditorView editorView = query('#editor-view').xtag;
    
    // ////////////////////////////////////
    // TODO: Replace Mock with REST data access
    // /////////////////////////////////////
    DataAccess dataAccess = new MockDataAccess();
    
    // Initialize the controller.
    _appController = new AppController(header, baseContainer, 
        navigationView, digestView, editorView, dataAccess);
    
    appController.buildUi();
  });
}