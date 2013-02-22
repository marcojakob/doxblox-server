package ch.documakery.web;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.documakery.domain.document.DocumentFolder;
import ch.documakery.service.DocumentFolderService;

/**
 * Controller for {@link DocumentFolder}s.
 */
@Controller
public class DocumentFolderController {
  
  private static final Logger LOG = LoggerFactory.getLogger(DocumentFolderController.class);
  
  private final DocumentFolderService documentFolderService;
  
  @Inject
  public DocumentFolderController(DocumentFolderService documentService) {
    this.documentFolderService = documentService;
  }

  @RequestMapping(value = "/documentfolders", method = RequestMethod.GET)
  @ResponseBody
  public List<DocumentFolder> getAllFoldersOfUser() {
    LOG.debug("Request to get all document folders of current user");
    List<DocumentFolder> result = documentFolderService.getAllFoldersOfUser();
    return result;
  }
  
  @RequestMapping(value = "/documentfolders", method = RequestMethod.POST)
  @ResponseBody
  public DocumentFolder createFolder(@RequestBody @Valid DocumentFolder folder) {
    LOG.debug("Reqest to create new document folder: {}", folder);
    return documentFolderService.saveWithUser(folder);
  }
}
