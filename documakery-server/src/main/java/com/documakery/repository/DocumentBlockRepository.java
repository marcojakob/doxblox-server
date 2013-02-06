package com.documakery.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.documakery.domain.document.DocumentBlock;

public interface DocumentBlockRepository extends MongoRepository<DocumentBlock, ObjectId> {
  
}
