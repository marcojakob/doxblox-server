/**
 * Sets up [EventBus] and events used by the [EventBus].
 */
library events;

import 'package:event_bus/event_bus.dart';
export 'package:event_bus/event_bus.dart';

import 'ui/digest/digest_cell.dart';
import 'model/model.dart';

EventBus _eventBus;

/// The global [EventBus] object.
EventBus get eventBus => _eventBus;

/// Initializes the global [EventBus] object. Should only be called once!
void init(EventBus eventBus) {
  _eventBus = eventBus;
}

/// Either a [Document], a [Document] and a [DocumentBlock], or none was 
/// selected. First element of the [List] is the [Document] or [:null:], the 
/// second element is the [DocumentBlock] or [:null:].
final EventType<List> documentAndBlockSelect = new EventType<List>();