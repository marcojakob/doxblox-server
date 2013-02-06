package com.documakery.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.documakery.domain.user.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {
  
  User findByEmail(String email);
}
