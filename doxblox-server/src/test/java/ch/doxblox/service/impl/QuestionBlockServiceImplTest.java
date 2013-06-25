package ch.doxblox.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ch.doxblox.UserTestUtils;
import ch.doxblox.domain.document.question.QuestionBlock;
import ch.doxblox.repository.QuestionBlockRepository;
import ch.doxblox.security.util.SecurityContextUtil;
import ch.doxblox.service.impl.QuestionBlockServiceImpl;

/**
 * Test for {@link QuestionBlockServiceImpl}.
 * 
 * @author Marco Jakob
 */
public class QuestionBlockServiceImplTest {

  // SUT //
  private QuestionBlockServiceImpl questionBlockService;

  private QuestionBlockRepository questionBlockRepository;
  private SecurityContextUtil securityContextUtilMock;

  @Before
  public void setUp() {
    questionBlockRepository = mock(QuestionBlockRepository.class);
    securityContextUtilMock = mock(SecurityContextUtil.class);
    
    questionBlockService = new QuestionBlockServiceImpl(securityContextUtilMock, questionBlockRepository);
  }
  
  @Test
  public void getAllQuestionBlocksOfUser_AsUser_CallsFindByUserId() {
    // given
    given(securityContextUtilMock.getCurrentUser()).willReturn(UserTestUtils.TEST_USER);
    
    // when
    questionBlockService.getAllQuestionBlocksOfUser();

    // then
    verify(questionBlockRepository, times(1)).findByUserId(UserTestUtils.TEST_USER_ID);
    verifyNoMoreInteractions(questionBlockRepository);
  }
  
  @Test
  public void saveWithUser_AsUser_UserIdIsAddedBeforeSave() {
    // given
    given(securityContextUtilMock.getCurrentUser()).willReturn(UserTestUtils.TEST_USER);
    QuestionBlock questionBlock = new QuestionBlock();
    
    // when
    questionBlockService.saveWithUser(questionBlock);

    // then
    ArgumentCaptor<QuestionBlock> questionBlockArgument = ArgumentCaptor.forClass(QuestionBlock.class);
    verify(questionBlockRepository, times(1)).save(questionBlockArgument.capture());
    verifyNoMoreInteractions(questionBlockRepository);
    
    assertThat(questionBlockArgument.getValue().getUserId(), is(UserTestUtils.TEST_USER_ID));
  }
}