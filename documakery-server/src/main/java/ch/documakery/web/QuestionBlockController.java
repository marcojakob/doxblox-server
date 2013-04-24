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

import ch.documakery.domain.document.question.QuestionBlock;
import ch.documakery.service.QuestionBlockService;

/**
 * Rest access to {@link QuestionBlock}s.
 */
@Controller
public class QuestionBlockController {
  
  private static final Logger LOG = LoggerFactory.getLogger(QuestionBlockController.class);
  
  private final QuestionBlockService questionBlockService;
  
  @Inject
  public QuestionBlockController(QuestionBlockService questionBlockService) {
    this.questionBlockService = questionBlockService;
  }

  @RequestMapping(value = "/questionblocks", method = RequestMethod.GET)
  @ResponseBody
  public List<QuestionBlock> getAllQuestionBlocksOfUser() {
    LOG.debug("Request to get all question blocks of current user");
    List<QuestionBlock> result = questionBlockService.getAllQuestionBlocksOfUser();
    return result;
  }
  
  @RequestMapping(value = "/questionblocks", method = RequestMethod.POST)
  @ResponseBody
  public QuestionBlock createQuestionBlock(@RequestBody @Valid QuestionBlock questionBlock) {
    LOG.debug("Request to create new question block: {}", questionBlock);
    return questionBlockService.saveWithUser(questionBlock);
  }
}
