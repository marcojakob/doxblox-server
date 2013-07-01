library doxblox.mock_data;

import 'dart:async';

import '../../model/model.dart';
import '../data.dart';

part 'mock_document_dao.dart';
part 'mock_document_folder_dao.dart';
part 'mock_document_block_dao.dart';

/**
 * Mock implmenentation of [DataAccess].
 */
class MockDataAccess implements DataAccess {
  DocumentDao documents = new MockDocumentDao();
  DocumentFolderDao documentFolders = new MockDocumentFolderDao();
  DocumentBlockDao documentBlocks = new MockDocumentBlockDao();
}

/**
 * Abstract base class for Data Access Objects.
 * Type [T] is the type of the data that is accessed.
 */
abstract class MockDao<T extends Persistable> extends Dao<T> {
  /// In-memory mock data.
  Map<String, T> data = {};
  
  int _idCounter = 0;
  
  /// Creates a new id and returns it.
  String newId() {
    _idCounter++;
    return _idCounter.toString();
  }
  
  /// Returns the last created id.
  String lastId() {
    return _idCounter.toString();
  }
  
  Future<T> create(T entity) {
    entity.id = newId();
    data[entity.id] = entity;
    return new Future.value(entity);
  }
  
  /**
   * Creates the new [entities]. Returns a list of the saved entities with the 
   * newly assigned ids.
   */
  Future<List<T>> createAll(List<T> entities) {
    entities.forEach((T entity) {
      entity.id = newId();
      data[entity.id] = entity;
    });
    return new Future.value(entities);
  }
  
  /**
   * Returns the entity identified by the given [id].
   */
  Future<T> getById(String id) {
    return new Future.value(data[id]);
  }
  
  /**
   * Returns all entities.
   */
  Future<List<T>> getAll() {
    return new Future.value(data.values.toList());
  }
  
  /**
   * Returns all entities with the given ids.
   */
  Future<List<T>> getAllByIds(List<String> ids) {
    List<T> result = new List();
    for (String id in ids) {
      result.add(data[id]);
    }
    return new Future.value(result);
  }
  
  /**
   * Updates the given [entity].
   */
  Future<bool> update(T entity) {
    data[entity.id] = entity;
    return new Future.value(true);
  }
  
  /**
   * Updates the given [entities].
   */
  Future<bool> updateAll(List<T> entities) {
    entities.forEach((T e) => data[e.id] = e);
    return new Future.value(true);
  }

  /**
   * Deletes the given [entity].
   */
  Future<bool> delete(T entity) {
    data.remove(entity.id);
    return new Future.value(true);
  }
  
  /**
   * Deletes the entity with the given [id].
   */
  Future<bool> deleteById(String id) {
    data.remove(id);
    return new Future.value(true);
  }
}
