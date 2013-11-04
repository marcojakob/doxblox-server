library doxblox.data;

import 'dart:async';
import 'package:logging/logging.dart';

import '../model/model.dart';

part 'document_dao.dart';
part 'document_folder_dao.dart';
part 'document_block_dao.dart';

final _log = new Logger("doxblox.data");

DataAccess _dataAccess;

/// The global [DataAccess] object.
DataAccess get dataAccess => _dataAccess;

/// Initializes the global [DataAccess] object. Should only be called once!
void init(DataAccess dataAccess) {
  _log.info('Initializing data access.');
  _dataAccess = dataAccess;
}

class DataAccess {
  DocumentDao documents;
  DocumentFolderDao documentFolders;
  DocumentBlockDao documentBlocks;
//  final DocumentDao documents = new DocumentDao();
//  final DocumentFolderDao documentFolders = new DocumentFolderDao();
//  final DocumentBlockDao documentBlocks = new DocumentBlockDao();
}

/**
 * Abstract base class for Data Access Objects.
 * Type [T] is the type of the data that is accessed.
 */
abstract class Dao<T extends Persistable> {
  
  /**
   * Creates the new [entity]. Returns the entity with a newly assigned id.
   */
  Future<T> create(T entity);
  
  /**
   * Creates the new [entities]. Returns a list of the saved entities with the 
   * newly assigned ids.
   */
  Future<List<T>> createAll(List<T> entities);
  
  /**
   * Returns the entity identified by the given [id].
   */
  Future<T> getById(String id);
  
  /**
   * Returns all entities.
   */
  Future<List<T>> getAll();
  
  /**
   * Returns all entities with the given ids.
   */
  Future<List<T>> getAllByIds(List<String> ids);
  
  /**
   * Updates the given [entity].
   */
  Future<bool> update(T entity);
  
  /**
   * Updates the given [entities].
   */
  Future<bool> updateAll(List<T> entities);

  /**
   * Deletes the given [entity].
   */
  Future<bool> delete(T entity);
  
  /**
   * Deletes the entity with the given [id].
   */
  Future<bool> deleteById(String id);
}
