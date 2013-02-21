package ch.documakery.service.impl;

import static org.mockito.BDDMockito.*;

import org.junit.Before;
import org.junit.Test;

import ch.documakery.UserTestUtils;
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
  public void getAllDocumentsOfUser() {
    // given
    given(securityContextUtilMock.getCurrentUser()).willReturn(UserTestUtils.TEST_USER);
    
    // when
    documentService.getAllDocumentsOfUser();

    // then
    verify(documentRepositoryMock, times(1)).findByUserId(UserTestUtils.ID);
    verifyNoMoreInteractions(documentRepositoryMock);
  }
}