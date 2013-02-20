package ch.documakery.service;

import java.util.List;

import ch.documakery.domain.document.Document;


/**
 * Service to handle {@link Document}s.
 */
public interface DocumentService {
  
  List<Document> getAllDocumentsOfUser();
}
