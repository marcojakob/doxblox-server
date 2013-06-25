package ch.doxblox.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import ch.doxblox.domain.document.DocumentFolder;
import ch.doxblox.domain.user.User;
import ch.doxblox.repository.DocumentFolderRepository;
import ch.doxblox.security.util.SecurityContextUtil;
import ch.doxblox.service.DocumentFolderService;


/**
 * Service to access {@link DocumentFolder}s in MongoDB repository.
 * 
 * @author Marco Jakob
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
