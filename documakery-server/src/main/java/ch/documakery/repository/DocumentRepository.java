package ch.documakery.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import ch.documakery.domain.document.Document;

public interface DocumentRepository extends MongoRepository<Document, ObjectId> {
  
  List<Document> findByUserId(ObjectId userId);
}
