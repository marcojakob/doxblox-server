package ch.documakery.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import ch.documakery.domain.document.Document;

/**
 * Repository to access {@link Document}s in MongoDB.
 * 
 * @author Marco Jakob
 */
public interface DocumentRepository extends MongoRepository<Document, ObjectId> {
  
  List<Document> findByUserId(ObjectId userId);
}
