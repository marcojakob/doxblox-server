package ch.doxblox.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ch.doxblox.UserTestUtils;
import ch.doxblox.domain.document.DocumentFolder;
import ch.doxblox.repository.DocumentFolderRepository;
import ch.doxblox.security.util.SecurityContextUtil;
import ch.doxblox.service.impl.DocumentFolderServiceImpl;

/**
 * Test for {@link DocumentFolderServiceImpl}.
 * 
 * @author Marco Jakob
 */
public class DocumentFolderServiceImplTest {

  // SUT //
  private DocumentFolderServiceImpl folderService;

  private DocumentFolderRepository folderRepositoryMock;
  private SecurityContextUtil securityContextUtilMock;

  @Before
  public void setUp() {
    folderRepositoryMock = mock(DocumentFolderRepository.class);
    securityContextUtilMock = mock(SecurityContextUtil.class);
    
    folderService = new DocumentFolderServiceImpl(securityContextUtilMock, folderRepositoryMock);
  }
  
  @Test
  public void getAllFoldersOfUser_AsUser_OnlyReturnsFoldersOfUser() {
    // given
    given(securityContextUtilMock.getCurrentUser()).willReturn(UserTestUtils.TEST_USER);
    
    // when
    folderService.getAllFoldersOfUser();

    // then
    verify(folderRepositoryMock, times(1)).findByUserId(UserTestUtils.TEST_USER_ID);
    verifyNoMoreInteractions(folderRepositoryMock);
  }
  
  @Test
  public void saveWithUser_AsUser_UserIdIsAddedBeforeSave() {
    // given
    given(securityContextUtilMock.getCurrentUser()).willReturn(UserTestUtils.TEST_USER);
    DocumentFolder folder = new DocumentFolder("Schoolyear 2012/2013");
    
    // when
    folderService.saveWithUser(folder);

    // then
    ArgumentCaptor<DocumentFolder> folderArgument = ArgumentCaptor.forClass(DocumentFolder.class);
    verify(folderRepositoryMock, times(1)).save(folderArgument.capture());
    verifyNoMoreInteractions(folderRepositoryMock);
    
    assertThat(folderArgument.getValue().getName(), is("Schoolyear 2012/2013"));
    assertThat(folderArgument.getValue().getUserId(), is(UserTestUtils.TEST_USER_ID));

  }
}