package ch.documakery.web;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.documakery.domain.document.Document;
import ch.documakery.domain.user.dto.UserDto;
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
    LOG.debug("Getting all documents of user");
    return null;
  }
//
//  @RequestMapping(value = "/user", method = RequestMethod.POST)
//  @ResponseBody
//  public UserDto registerUser(@RequestBody @Valid UserRegisterDto userRegister) {
//    LOG.debug("Registering user: {}", userRegister.getEmail());
//    User user = userService.register(userRegister);
//    return new UserDto(user);
//  }
//  
//  @RequestMapping(value = "/user", method = RequestMethod.DELETE)
//  @ResponseBody
//  public void deleteUser() {
//    LOG.info("Deleging user.");
//    userService.deleteUser();
//  }
  
}
