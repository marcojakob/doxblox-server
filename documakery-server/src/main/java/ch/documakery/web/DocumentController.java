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

import ch.documakery.domain.document.Document;
import ch.documakery.service.DocumentService;

/**
 * Controller for {@link Document}s.
 */
@Controller
public class DocumentController {
  
  private static final Logger LOG = LoggerFactory.getLogger(DocumentController.class);
  
  private final DocumentService documentService;
  
  @Inject
  public DocumentController(DocumentService documentService) {
    this.documentService = documentService;
  }

  @RequestMapping(value = "/document", method = RequestMethod.GET)
  @ResponseBody
  public List<Document> getAllDocumentsOfUser() {
    LOG.debug("Request to get all documents of current user");
    List<Document> result = documentService.getAllDocumentsOfUser();
    return result;
  }
  
  @RequestMapping(value = "/document", method = RequestMethod.POST)
  @ResponseBody
  public Document saveDocument(@RequestBody @Valid Document document) {
    LOG.debug("Reqest to save document : {}", document);
    return documentService.saveWithUser(document);
  }
}
