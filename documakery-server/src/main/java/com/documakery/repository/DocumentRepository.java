package com.documakery.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.documakery.domain.document.Document;

public interface DocumentRepository extends MongoRepository<Document, ObjectId> {
  
}
