package ch.doxblox.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import ch.doxblox.domain.document.question.QuestionBlock;

/**
 * Repository to access {@link QuestionBlock}s in MongoDB.
 * 
 * @author Marco Jakob
 */
public interface QuestionBlockRepository extends MongoRepository<QuestionBlock, ObjectId> {
  
  List<QuestionBlock> findByUserId(ObjectId userId);
}
