package ch.documakery.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import ch.documakery.domain.document.Document;
import ch.documakery.repository.DocumentRepository;
import ch.documakery.service.DocumentService;


/**
 * Document Service implementation.
 */
@Service
public class DocumentServiceImpl implements DocumentService {

  private DocumentRepository repository;
  
  @Inject
  public DocumentServiceImpl(DocumentRepository repository) {
    this.repository = repository;
  }

  public List<Document> findAll() {
    return repository.findAll();
  }
}
