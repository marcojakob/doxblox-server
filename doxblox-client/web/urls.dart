/**
 * Sets up routing of urls.
 */
library doxblox.urls;

import 'package:route/client.dart';
export 'package:route/client.dart';

import 'dart:html';

Router _router;

/// The global [Router] object.
Router get router => _router;

/// Initializes the global [Router] object. Should only be called once!
void init(Router router) {
  _router = router;
}

// Get initial path.
final String pathname = window.location.pathname;

final UrlPattern home = new UrlPattern(pathname + r'');
final UrlPattern document = new UrlPattern(pathname + r'#document/(\d+)');
final UrlPattern documentBlock = new UrlPattern(pathname + r'#document/(\d+)/block/(\d+)');