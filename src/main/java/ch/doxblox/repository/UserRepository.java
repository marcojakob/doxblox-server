package ch.doxblox.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import ch.doxblox.domain.user.User;

/**
 * Repository for {@link User} entities.
 * 
 * @author Marco Jakob
 */
public interface UserRepository extends MongoRepository<User, ObjectId> {
  
  User findByEmail(String email);
  
  User findByNickname(String nickname);
}
