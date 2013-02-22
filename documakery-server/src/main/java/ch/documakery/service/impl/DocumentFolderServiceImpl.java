package ch.documakery.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import ch.documakery.domain.document.DocumentFolder;
import ch.documakery.domain.user.User;
import ch.documakery.repository.DocumentFolderRepository;
import ch.documakery.security.util.SecurityContextUtil;
import ch.documakery.service.DocumentFolderService;


/**
 * {@link DocumentFolder} Service implementation.
 */
@Service
public class DocumentFolderServiceImpl implements DocumentFolderService {

  private DocumentFolderRepository folderRepository;
  private SecurityContextUtil securityContextUtil;

  @Inject
  public DocumentFolderServiceImpl(SecurityContextUtil securityContextUtil,
      DocumentFolderRepository folderRepository) {
    this.securityContextUtil = securityContextUtil;
    this.folderRepository = folderRepository;
  }

  @Override
  public List<DocumentFolder> getAllFoldersOfUser() {
    User user = securityContextUtil.getCurrentUser();
    return folderRepository.findByUserId(user.getId());
  }

  @Override
  public DocumentFolder saveWithUser(DocumentFolder folder) {
    User user = securityContextUtil.getCurrentUser();
    folder.setUserId(user.getId());
    return folderRepository.save(folder);
  }
}
