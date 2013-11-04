/**
 * Sets up [EventBus] and events used by the [EventBus].
 */
library doxblox.events;

import 'package:logging/logging.dart';

import 'package:event_bus/event_bus.dart';
export 'package:event_bus/event_bus.dart';

import 'model/model.dart';

final _log = new Logger('doxblox.events');

EventBus _eventBus;

/// The global [EventBus] object.
EventBus get eventBus => _eventBus;

/// Initializes the global [EventBus] object. Should only be called once!
void init(EventBus eventBus) {
  _log.info('Initializing event bus.');
  _eventBus = eventBus;
}


// -------------------
// Events
// -------------------

/// A [Document] was selected. If a selection was cleared, the data is null.
final EventType<Document> documentSelect = new EventType<Document>();

/// A [DocumentBlock] was selected. If a selection was cleared, the data is null.
final EventType<DocumentBlock> documentBlockSelect = new EventType<DocumentBlock>();