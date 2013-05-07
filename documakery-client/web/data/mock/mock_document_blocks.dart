part of mock_data;

Map<String, QuestionBlock> mockQuestionBlocks() {
  return {
  '1' : new QuestionBlock()
  ..id = '1' 
  ..title = 'QuestionBlock 1' 
  ..introduction = 'QuestionBlock Intro' 
  ..questions = [mockTextQuestion('1a'), mockTextQuestion('1b'), mockTextQuestion('1c'), mockTextQuestion('1d')]
  ..topics = ['1', '2']
  ..libraryType = 'PUBLIC'
  ..userId = '9999',
  
  '2' : new QuestionBlock()
  ..id = '2' 
  ..title = 'QuestionBlock 2' 
  ..introduction = 'QuestionBlock Intro' 
  ..questions = [mockTextQuestion('2a'), mockTextQuestion('2b'), mockTextQuestion('2c'), mockTextQuestion('2d')]
  ..topics = ['1', '2']
  ..libraryType = 'PUBLIC'
  ..userId = '9999',
  
  '3' : new QuestionBlock()
  ..id = '3' 
  ..title = 'QuestionBlock 3' 
  ..introduction = 'QuestionBlock Intro' 
  ..questions = [mockTextQuestion('3a'), mockTextQuestion('3b'), mockTextQuestion('3c'), mockTextQuestion('3d')]
  ..topics = ['1', '2']
  ..libraryType = 'PUBLIC'
  ..userId = '9999',
  
  '4' : new QuestionBlock()
  ..id = '4' 
  ..title = 'QuestionBlock 4' 
  ..introduction = 'QuestionBlock Intro' 
  ..questions = [mockTextQuestion('4a'), mockTextQuestion('4b')]
  ..topics = ['1', '2']
  ..libraryType = 'PUBLIC'
  ..userId = '9999',
  
  '5' : new QuestionBlock()
  ..id = '5' 
  ..title = 'QuestionBlock 5' 
  ..introduction = 'QuestionBlock Intro' 
  ..questions = [mockTextQuestion('5a'), mockTextQuestion('5b')]
  ..topics = ['1', '2']
  ..libraryType = 'PUBLIC'
  ..userId = '9999',
  
  '6' : new QuestionBlock()
  ..id = '6' 
  ..title = 'QuestionBlock 6' 
  ..introduction = 'QuestionBlock Intro' 
  ..questions = [mockTextQuestion('6a'), mockTextQuestion('6b')]
  ..topics = ['1', '2']
  ..libraryType = 'PUBLIC'
  ..userId = '9999',
  
  '7' : new QuestionBlock()
  ..id = '7' 
  ..title = 'QuestionBlock 7' 
  ..introduction = 'QuestionBlock Intro' 
  ..questions = [mockTextQuestion('7a'), mockTextQuestion('7b')]
  ..topics = ['1', '2']
  ..libraryType = 'PUBLIC'
  ..userId = '9999',
  
  '8' : new QuestionBlock()
  ..id = '8' 
  ..title = 'QuestionBlock 8' 
  ..introduction = 'QuestionBlock Intro' 
  ..questions = [mockTextQuestion('8a'), mockTextQuestion('8b')]
  ..topics = ['1', '2']
  ..libraryType = 'PUBLIC'
  ..userId = '9999',
  
  '9' : new QuestionBlock()
  ..id = '9' 
  ..title = 'QuestionBlock 9' 
  ..introduction = 'QuestionBlock Intro' 
  ..questions = [mockTextQuestion('9a'), mockTextQuestion('9b')]
  ..topics = ['1', '2']
  ..libraryType = 'PUBLIC'
  ..userId = '9999',
  };
}

/**
 * Creates a mock for [TextQuestion] appending [str].
 */
TextQuestion mockTextQuestion(String str) {
  return new TextQuestion()
  ..text = 'Question Text $str'
  ..points = 3
  ..solution = 'Solution $str'
  ..emptyAnswerLines = 5;
}











