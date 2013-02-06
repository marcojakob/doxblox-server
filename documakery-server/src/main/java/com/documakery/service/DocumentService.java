package com.documakery.service;

import java.util.List;

import com.documakery.domain.document.Document;


/**
 * Service to handle {@link Document}s.
 */
public interface DocumentService {
  
  List<Document> findAll();
}
