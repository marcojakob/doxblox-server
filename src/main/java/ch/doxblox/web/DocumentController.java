package ch.doxblox.web;

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

import ch.doxblox.domain.document.Document;
import ch.doxblox.service.DocumentService;

/**
 * REST access to {@link Document}s.
 */
@Controller
public class DocumentController {
  
  private static final Logger LOG = LoggerFactory.getLogger(DocumentController.class);
  
  private final DocumentService documentService;
  
  @Inject
  public DocumentController(DocumentService documentService) {
    this.documentService = documentService;
  }

  @RequestMapping(value = "/documents", method = RequestMethod.GET)
  @ResponseBody
  public List<Document> getAllDocumentsOfUser() {
    LOG.debug("Request to get all documents of current user");
    List<Document> result = documentService.getAllDocumentsOfUser();
    return result;
  }
  
  @RequestMapping(value = "/documents", method = RequestMethod.POST)
  @ResponseBody
  public Document createDocument(@RequestBody @Valid Document document) {
    LOG.debug("Reqest to create new document : {}", document);
    return documentService.saveWithUser(document);
  }
}
