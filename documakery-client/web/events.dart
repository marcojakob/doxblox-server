/**
 * Set up events used by the [EventBus].
 */
library events;

import 'package:event_bus/event_bus.dart';
export 'package:event_bus/event_bus.dart';

import 'ui/digest/digest_cell.dart';
import 'model/model.dart';

/// The global [EventBus].
final EventBus eventBus = new EventBus();

final EventType<DocumentBlock> digestViewDocumentBlockSelected = new EventType<DocumentBlock>();

final EventType<Document> navigationViewDocumentSelected = new EventType<Document>();
