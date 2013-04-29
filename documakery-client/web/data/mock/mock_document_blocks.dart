part of mock_data;

Map<String, QuestionBlock> mockQuestionBlocks() {
  return {
  '1' : new QuestionBlock()
  ..id = '1' 
  ..title = 'QuestionBlock 1' 
  ..introduction = 'QuestionBlock Intro' 
  ..questions = [mockTextAnswerQuestion('1a'), mockTextAnswerQuestion('1b')]
  ..topics = ['1', '2']
  ..libraryType = 'PUBLIC'
  ..userId = '9999',
  
  '2' : new QuestionBlock()
  ..id = '2' 
  ..title = 'QuestionBlock 2' 
  ..introduction = 'QuestionBlock Intro' 
  ..questions = [mockTextAnswerQuestion('2a'), mockTextAnswerQuestion('2b')]
  ..topics = ['1', '2']
  ..libraryType = 'PUBLIC'
  ..userId = '9999',
  
  '3' : new QuestionBlock()
  ..id = '3' 
  ..title = 'QuestionBlock 3' 
  ..introduction = 'QuestionBlock Intro' 
  ..questions = [mockTextAnswerQuestion('3a'), mockTextAnswerQuestion('3b')]
  ..topics = ['1', '2']
  ..libraryType = 'PUBLIC'
  ..userId = '9999',
  
  '4' : new QuestionBlock()
  ..id = '4' 
  ..title = 'QuestionBlock 4' 
  ..introduction = 'QuestionBlock Intro' 
  ..questions = [mockTextAnswerQuestion('4a'), mockTextAnswerQuestion('4b')]
  ..topics = ['1', '2']
  ..libraryType = 'PUBLIC'
  ..userId = '9999',
  
  '5' : new QuestionBlock()
  ..id = '5' 
  ..title = 'QuestionBlock 5' 
  ..introduction = 'QuestionBlock Intro' 
  ..questions = [mockTextAnswerQuestion('5a'), mockTextAnswerQuestion('5b')]
  ..topics = ['1', '2']
  ..libraryType = 'PUBLIC'
  ..userId = '9999',
  
  '6' : new QuestionBlock()
  ..id = '6' 
  ..title = 'QuestionBlock 6' 
  ..introduction = 'QuestionBlock Intro' 
  ..questions = [mockTextAnswerQuestion('6a'), mockTextAnswerQuestion('6b')]
  ..topics = ['1', '2']
  ..libraryType = 'PUBLIC'
  ..userId = '9999',
  
  '7' : new QuestionBlock()
  ..id = '7' 
  ..title = 'QuestionBlock 7' 
  ..introduction = 'QuestionBlock Intro' 
  ..questions = [mockTextAnswerQuestion('7a'), mockTextAnswerQuestion('7b')]
  ..topics = ['1', '2']
  ..libraryType = 'PUBLIC'
  ..userId = '9999',
  
  '8' : new QuestionBlock()
  ..id = '8' 
  ..title = 'QuestionBlock 8' 
  ..introduction = 'QuestionBlock Intro' 
  ..questions = [mockTextAnswerQuestion('8a'), mockTextAnswerQuestion('8b')]
  ..topics = ['1', '2']
  ..libraryType = 'PUBLIC'
  ..userId = '9999',
  
  '9' : new QuestionBlock()
  ..id = '9' 
  ..title = 'QuestionBlock 9' 
  ..introduction = 'QuestionBlock Intro' 
  ..questions = [mockTextAnswerQuestion('9a'), mockTextAnswerQuestion('9b')]
  ..topics = ['1', '2']
  ..libraryType = 'PUBLIC'
  ..userId = '9999',
  };
}

/**
 * Creates a mock for [TextAnswerQuestion] appending [str].
 */
TextAnswerQuestion mockTextAnswerQuestion(String str) {
  return new TextAnswerQuestion()
  ..text = 'Question Text $str'
  ..correctionNotes = 'Correction Notes $str'
  ..points = 3
  ..emptyAnswerLines = 5;
}











