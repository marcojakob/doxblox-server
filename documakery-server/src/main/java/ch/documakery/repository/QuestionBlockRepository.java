package ch.documakery.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import ch.documakery.domain.document.QuestionBlock;

public interface QuestionBlockRepository extends MongoRepository<QuestionBlock, ObjectId> {
  
}
