package ch.documakery.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import ch.documakery.domain.document.Document;
import ch.documakery.domain.user.User;
import ch.documakery.repository.DocumentRepository;
import ch.documakery.security.util.SecurityContextUtil;
import ch.documakery.service.DocumentService;


/**
 * Service to access {@link Document}s in MongoDB repository.
 * 
 * @author Marco Jakob
 */
@Service
public class DocumentServiceImpl implements DocumentService {

  private DocumentRepository documentRepository;
  private SecurityContextUtil securityContextUtil;
  
  @Inject
  public DocumentServiceImpl(SecurityContextUtil securityContextUtil, DocumentRepository repository) {
    this.securityContextUtil = securityContextUtil;
    this.documentRepository = repository;
  }

  @Override
  public List<Document> getAllDocumentsOfUser() {
    User user = securityContextUtil.getCurrentUser();
    return documentRepository.findByUserId(user.getId());
  }

  @Override
  public Document saveWithUser(Document document) {
    User user = securityContextUtil.getCurrentUser();
    document.setUserId(user.getId());
    return documentRepository.save(document);
  }
}
