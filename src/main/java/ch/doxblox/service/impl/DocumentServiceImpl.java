package ch.doxblox.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import ch.doxblox.domain.document.Document;
import ch.doxblox.domain.user.User;
import ch.doxblox.repository.DocumentRepository;
import ch.doxblox.security.util.SecurityContextUtil;
import ch.doxblox.service.DocumentService;


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
