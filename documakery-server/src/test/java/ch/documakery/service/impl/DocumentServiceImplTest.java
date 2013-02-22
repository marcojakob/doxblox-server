package ch.documakery.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ch.documakery.UserTestUtils;
import ch.documakery.domain.document.Document;
import ch.documakery.repository.DocumentRepository;
import ch.documakery.security.util.SecurityContextUtil;

/**
 * Test for {@link DocumentServiceImpl}.
 * 
 * @author Marco Jakob
 */
public class DocumentServiceImplTest {

  // SUT //
  private DocumentServiceImpl documentService;

  private DocumentRepository documentRepositoryMock;
  private SecurityContextUtil securityContextUtilMock;

  @Before
  public void setUp() {
    documentRepositoryMock = mock(DocumentRepository.class);
    securityContextUtilMock = mock(SecurityContextUtil.class);
    
    documentService = new DocumentServiceImpl(securityContextUtilMock, documentRepositoryMock);
  }
  
  @Test
  public void getAllDocumentsOfUser_AsUser_OnlyReturnsDocumentsOfUser() {
    // given
    given(securityContextUtilMock.getCurrentUser()).willReturn(UserTestUtils.TEST_USER);
    
    // when
    documentService.getAllDocumentsOfUser();

    // then
    verify(documentRepositoryMock, times(1)).findByUserId(UserTestUtils.TEST_USER_ID);
    verifyNoMoreInteractions(documentRepositoryMock);
  }
  
  @Test
  public void saveWithUser_AsUser_UserIdIsAddedBeforeSave() {
    // given
    given(securityContextUtilMock.getCurrentUser()).willReturn(UserTestUtils.TEST_USER);
    Document document = new Document();
    document.setName("My Doc Name");
    
    // when
    documentService.saveWithUser(document);

    // then
    ArgumentCaptor<Document> documentArgument = ArgumentCaptor.forClass(Document.class);
    verify(documentRepositoryMock, times(1)).save(documentArgument.capture());
    verifyNoMoreInteractions(documentRepositoryMock);
    
    assertThat(documentArgument.getValue().getName(), is("My Doc Name"));
    assertThat(documentArgument.getValue().getUserId(), is(UserTestUtils.TEST_USER_ID));

  }
}