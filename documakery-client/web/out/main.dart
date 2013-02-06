// Auto-generated from main.html.
// DO NOT EDIT.

library main;

import 'dart:html' as autogenerated_html;
import 'dart:web_audio' as autogenerated_audio;
import 'dart:svg' as autogenerated_svg;
import 'package:web_ui/web_ui.dart' as autogenerated;

import 'dart:html';

import '../model.dart';

import 'package:web_ui/web_ui.dart';

import 'todo_row.html.dart';
import 'router_options.html.dart';


// Original code
main() {
  // listen on changes to #hash in the URL
  // Note: listen on both popState and hashChange, because IE9 doens't support
  // history API, and it doesn't work properly on Opera 12.
  // See http://dartbug.com/5483
  updateFilters(e) {
    viewModel.showIncomplete = window.location.hash != '#/completed';
    viewModel.showDone = window.location.hash != '#/active';
    dispatch();
  }
  window.on.hashChange.add(updateFilters);
  window.on.popState.add(updateFilters);
}

void addTodo(Event e) {
  e.preventDefault(); // don't submit the form
  var input = query('#new-todo');
  if (input.value == '') return;
  app.todos.add(new Todo(input.value));
  input.value = '';
}


// Additional generated code
/** Create the views and bind them to models. */
void init_autogenerated() {
  var _root = autogenerated_html.document.body;
  autogenerated_html.Element __todoapp;
  
  autogenerated_html.Element __header;
  
  autogenerated_html.FormElement __e0;
  
  autogenerated_html.EventListener __listener__e0_submit_1;
  
  autogenerated_html.InputElement __newTodo;
  
  autogenerated_html.EventListener __listener__newTodo_change_2;
  
  autogenerated_html.Element __main;
  
  autogenerated_html.InputElement __toggleAll;
  
  autogenerated_html.EventListener __listener__toggleAll_change_3;
  
  List<autogenerated.WatcherDisposer> __stoppers4;
  
  autogenerated_html.UListElement __todoList;
  
  autogenerated_html.Element __e3;
  
  List<Function> _removeChild__e3 = [];
  
  autogenerated_html.Node _endPosition__e3;
  
  autogenerated_html.Element __e8;
  
  autogenerated_html.Node _endPosition__e8;
  
  bool _isVisible__e8 = false;
  
  autogenerated_html.Element __footer;
  
  autogenerated_html.SpanElement __todoCount;
  
  autogenerated_html.Element __e5;
  
  var __binding4;
  
  List<autogenerated.WatcherDisposer> __stoppers7_1;
  
  autogenerated_html.UnknownElement __filters;
  
  autogenerated_html.Element __e7;
  
  autogenerated_html.Node _endPosition__e7;
  
  bool _isVisible__e7 = false;
  
  autogenerated_html.ButtonElement __clearCompleted;
  
  autogenerated_html.EventListener __listener__clearCompleted_click_8_2_1;
  
  var __binding6;
  
  List<autogenerated.WatcherDisposer> __stoppers9_3_2;
  
  autogenerated_html.Element __info;
  


  // Initialize fields.
  __todoapp = _root.query('#todoapp');
  __header = __todoapp.query('#header');
  __e0 = __header.query('#__e-0');
  __newTodo = __e0.query('#new-todo');
  __main = __todoapp.query('#main');
  __toggleAll = __main.query('#toggle-all');
  __stoppers4 = [];
  __todoList = __main.query('#todo-list');
  __e3 = __todoList.query('#__e-3');
  __e8 = __todoapp.query('#__e-8');
  __info = _root.query('#info');
  

  // Attach model to views.
  __listener__e0_submit_1 = ($event) {
    addTodo($event);
    autogenerated.dispatch();
  };
  __e0.on.submit.add(__listener__e0_submit_1);
  
  __listener__newTodo_change_2 = ($event) {
    addTodo($event);
    autogenerated.dispatch();
  };
  __newTodo.on.change.add(__listener__newTodo_change_2);
  
  __listener__toggleAll_change_3 = ($event) {
    app.allChecked = __toggleAll.checked;
    autogenerated.dispatch();
  };
  __toggleAll.on.change.add(__listener__toggleAll_change_3);
  
  __stoppers4.add(autogenerated.watchAndInvoke(() => app.allChecked, (__e) { __toggleAll.checked = __e.newValue; }));
  
  _endPosition__e3 = __e3;
  
  __stoppers4.add(autogenerated.watchAndInvoke(() => app.todos, (_) {
    for (var remover in _removeChild__e3) remover();
    _removeChild__e3.clear();
    
    _endPosition__e3 = autogenerated.removeNodes(__e3, _endPosition__e3);
    
    var __insert___e3 = __e3.nextNode;
    
    for (var x in app.todos) {
      
      autogenerated_html.Element __e2;
      List<autogenerated.WatcherDisposer> __stoppers5_1;
      autogenerated_html.Node _endPosition__e2;
      bool _isVisible__e2 = false;
      autogenerated_html.UnknownElement __e1;
      List<autogenerated.WatcherDisposer> __stoppers6_2_1;
      
      __e2 = new autogenerated_html.Element.html('<template style="display:none"></template>');
      __stoppers5_1 = [];
      
      autogenerated.insertAllBefore(__e3.parentNode, __insert___e3,
      
      [new autogenerated_html.Text('\n          '), __e2, _endPosition__e3 = new autogenerated_html.Text('\n        ')]);
      
      _endPosition__e2 = __e2;
      __stoppers5_1.add(autogenerated.watchAndInvoke(() => viewModel.isVisible(x), (__e) {
        bool showNow = __e.newValue;
        if (_isVisible__e2 && !showNow) {
          _isVisible__e2 = false;
          
          __e1.xtag..removed_autogenerated()
          ..removed();
          (__stoppers6_2_1..forEach((s) => s())).clear();
          __e1 = null;
          _endPosition__e2 = autogenerated.removeNodes(__e2, _endPosition__e2);
        } else if (!_isVisible__e2 && showNow) {
          _isVisible__e2 = true;
          __e1 = new autogenerated_html.Element.tag('x-todo-row');
          __stoppers6_2_1 = [];
          new TodoRow.forElement(__e1)
          ..created_autogenerated()
          ..created()
          ..composeChildren();
          autogenerated.insertAllBefore(__e2.parentNode, __e2.nextNode,
          [new autogenerated_html.Text('\n            '), __e1, _endPosition__e2 = new autogenerated_html.Text('\n          ')]);
          __stoppers6_2_1.add(autogenerated.watchAndInvoke(() => x, (__e) { __e1.xtag.todo = __e.newValue; }));
          __e1.xtag..inserted()
          ..inserted_autogenerated();
          
        }
      }));
      
      _removeChild__e3.add(() {
        
        (__stoppers5_1..forEach((s) => s())).clear();
        if (_isVisible__e2) {
          _endPosition__e2 = autogenerated.removeNodes(__e2, _endPosition__e2);
          __e1.xtag..removed_autogenerated()
          ..removed();
          (__stoppers6_2_1..forEach((s) => s())).clear();
          __e1 = null;
        }
        __e2 = null;
        
      });
    }
  }));
  
  _endPosition__e8 = __e8;
  
  __stoppers4.add(autogenerated.watchAndInvoke(() => app.todos.length > 0, (__e) {
    bool showNow = __e.newValue;
    if (_isVisible__e8 && !showNow) {
      _isVisible__e8 = false;
      
      __footer = null;
      __todoCount = null;
      __e5 = null;
      (__stoppers7_1..forEach((s) => s())).clear();
      __binding4 = null;
      __filters.xtag..removed_autogenerated()
      ..removed();
      __filters = null;
      if (_isVisible__e7) {
        _endPosition__e7 = autogenerated.removeNodes(__e7, _endPosition__e7);
        __clearCompleted.on.click.remove(__listener__clearCompleted_click_8_2_1);
        __listener__clearCompleted_click_8_2_1 = null;
        
        __clearCompleted = null;
        (__stoppers9_3_2..forEach((s) => s())).clear();
        __binding6 = null;
      }
      __e7 = null;
      
      _endPosition__e8 = autogenerated.removeNodes(__e8, _endPosition__e8);
      
    } else if (!_isVisible__e8 && showNow) {
      
      _isVisible__e8 = true;
      
      __footer = new autogenerated_html.Element.html('<footer id="footer">\n        <span id="todo-count"><strong id="__e-5"></strong></span>\n        <x-router-options id="filters">\n          <li> <a href="#/">All</a> </li>\n          <li> <a href="#/active">Active</a> </li>\n          <li> <a href="#/completed">Completed</a> </li>\n        </x-router-options>\n        <template id="__e-7" style="display:none"></template>\n      </footer>');
      __todoCount = __footer.query('#todo-count');
      __e5 = __todoCount.query('#__e-5');
      __binding4 = new autogenerated_html.Text('');
      __stoppers7_1 = [];
      __e5.nodes.add(__binding4);
      __filters = __footer.query('#filters');
      new RouterOptions.forElement(__filters)
      ..created_autogenerated()
      ..created()
      ..composeChildren();
      __e7 = __footer.query('#__e-7');
      
      autogenerated.insertAllBefore(__e8.parentNode, __e8.nextNode,
      
      [new autogenerated_html.Text('\n      '), __footer, _endPosition__e8 = new autogenerated_html.Text('\n    ')]);
      
      __stoppers7_1.add(autogenerated.watchAndInvoke(() => '${app.remaining}', (__e) {
        __binding4 = autogenerated.updateBinding(app.remaining, __binding4, __e.newValue);
      }));
      __filters.xtag..inserted()
      ..inserted_autogenerated();
      _endPosition__e7 = __e7;
      __stoppers7_1.add(autogenerated.watchAndInvoke(() => app.doneCount > 0, (__e) {
        bool showNow = __e.newValue;
        if (_isVisible__e7 && !showNow) {
          _isVisible__e7 = false;
          
          __clearCompleted.on.click.remove(__listener__clearCompleted_click_8_2_1);
          __listener__clearCompleted_click_8_2_1 = null;
          
          __clearCompleted = null;
          (__stoppers9_3_2..forEach((s) => s())).clear();
          __binding6 = null;
          _endPosition__e7 = autogenerated.removeNodes(__e7, _endPosition__e7);
        } else if (!_isVisible__e7 && showNow) {
          _isVisible__e7 = true;
          __clearCompleted = new autogenerated_html.Element.html('<button id="clear-completed"></button>');
          __binding6 = new autogenerated_html.Text('');
          __stoppers9_3_2 = [];
          __clearCompleted.nodes.add(new autogenerated_html.Text('\n            Clear completed ('));
          __clearCompleted.nodes.add(__binding6);
          __clearCompleted.nodes.add(new autogenerated_html.Text(')\n          '));
          autogenerated.insertAllBefore(__e7.parentNode, __e7.nextNode,
          [new autogenerated_html.Text('\n          '), __clearCompleted, _endPosition__e7 = new autogenerated_html.Text('\n        ')]);
          __listener__clearCompleted_click_8_2_1 = ($event) {
            app.clearDone();
            autogenerated.dispatch();
          };
          __clearCompleted.on.click.add(__listener__clearCompleted_click_8_2_1);
          
          __stoppers9_3_2.add(autogenerated.watchAndInvoke(() => '${app.doneCount}', (__e) {
            __binding6 = autogenerated.updateBinding(app.doneCount, __binding6, __e.newValue);
          }));
          
        }
      }));
      
    }
  }));
  

}