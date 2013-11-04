library doxblox.init;

import 'package:intl/intl.dart';
import 'package:polymer/polymer.dart';
import 'package:logging/logging.dart';

import 'data/mock/mock_data.dart';
import 'data/data.dart' as data;
import 'events.dart' as events;

final _log = new Logger("doxblox.init");

main() {
  _initLogging();
  
  // TODO: Replace Mock with REST data access
  data.init(new MockDataAccess());
  
  events.init(new events.EventBus());
  
  _log.info('Initializing polymer.');
  initPolymer();
}

/// Initializes the logging handler and log level.
void _initLogging() {
  hierarchicalLoggingEnabled = true;

  DateFormat dateFormat = new DateFormat('yyyy.mm.dd HH:mm:ss.SSS');
  
  // Print output to console.
  Logger.root.onRecord.listen((LogRecord r) {
    print('${dateFormat.format(r.time)}\t${r.loggerName}\t[${r.level.name}]:\t${r.message}');
  });
  
  // Root logger level.
  Logger.root.level = Level.INFO;
  
  // Doxblox logger level (affects all loggers starting with 'doxblox.').
  new Logger('doxblox')..level = Level.ALL;
  
  _log.info('Logging initialized.');
}