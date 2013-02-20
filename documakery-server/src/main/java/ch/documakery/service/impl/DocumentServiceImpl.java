package ch.documakery.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import ch.documakery.domain.document.Document;
import ch.documakery.repository.DocumentRepository;
import ch.documakery.service.DocumentService;
import ch.documakery.service.UserService;


/**
 * Document Service implementation.
 */
@Service
public class DocumentServiceImpl implements DocumentService {

  private DocumentRepository repository;
  private UserService userService;
  
  @Inject
  public DocumentServiceImpl(UserService userService, DocumentRepository repository) {
    this.userService = userService;
    this.repository = repository;
  }

  @Override
  public List<Document> getAllDocumentsOfUser() {
//    userService.getUser
    return null;
  }
}
