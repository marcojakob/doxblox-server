package ch.documakery.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import ch.documakery.domain.document.DocumentFolder;

/**
 * Repository to access {@link DocumentFolder}s in MongoDB.
 * 
 * @author Marco Jakob
 */
public interface DocumentFolderRepository extends MongoRepository<DocumentFolder, ObjectId> {
  
  List<DocumentFolder> findByUserId(ObjectId userId);
}
