package ch.documakery.web;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.documakery.UserTestUtils;
import ch.documakery.domain.document.Document;
import ch.documakery.domain.user.User;
import ch.documakery.domain.user.dto.UserDto;
import ch.documakery.domain.user.dto.UserRegisterDto;
import ch.documakery.service.DocumentService;
import ch.documakery.service.UserService;

/**
 * Test for {@link DocumentController}.
 * 
 * @author Marco Jakob
 */
public class DocumentControllerTest {

  private DocumentController documentController; // SUT

  private DocumentService documentServiceMock;

  @Before
  public void setUp() {
    documentServiceMock = mock(DocumentService.class);
    documentController = new DocumentController(documentServiceMock);
  }

  @Test
  public void getAllDocumentsOfUser() {
    // given
//    given(documentServiceMock.getPrincipal()).willReturn(UserTestUtils.createPrincipal());

    // when
    List<Document> documents = documentController.getAllDocumentsOfUser();

    // then
//    verify(documentServiceMock, times(1)).getPrincipal();
    verifyNoMoreInteractions(documentServiceMock);

    assertThat(documents.size(), is(4));
  }
}
