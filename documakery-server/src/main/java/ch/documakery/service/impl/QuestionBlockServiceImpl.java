package ch.documakery.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import ch.documakery.domain.document.QuestionBlock;
import ch.documakery.domain.user.User;
import ch.documakery.repository.QuestionBlockRepository;
import ch.documakery.security.util.SecurityContextUtil;
import ch.documakery.service.QuestionBlockService;


/**
 * Service to access {@link QuestionBlock}s in MongoDB repository.
 * 
 * @author Marco Jakob
 */
@Service
public class QuestionBlockServiceImpl implements QuestionBlockService {

  private QuestionBlockRepository questionBlockRepository;
  private SecurityContextUtil securityContextUtil;

  @Inject
  public QuestionBlockServiceImpl(SecurityContextUtil securityContextUtil,
      QuestionBlockRepository questionBlockRepository) {
    this.securityContextUtil = securityContextUtil;
    this.questionBlockRepository = questionBlockRepository;
  }
  
  @Override
  public List<QuestionBlock> getAllQuestionBlocksOfUser() {
    User user = securityContextUtil.getCurrentUser();
    return questionBlockRepository.findByUserId(user.getId());
  }

  @Override
  public QuestionBlock saveWithUser(QuestionBlock questionBlock) {
    User user = securityContextUtil.getCurrentUser();
    questionBlock.setUserId(user.getId());
    return questionBlockRepository.save(questionBlock);
  }
}
