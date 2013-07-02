part of doxblox.mock_data;

class MockDocumentBlockDao extends MockDao implements DocumentBlockDao {
  
  MockDocumentBlockDao() {
    
    data[newId()] = new QuestionBlock()
    ..id = lastId() 
    ..title = 'QuestionBlock 1' 
    ..introduction = 'QuestionBlock Intro' 
    ..questions = [_textQuestion('1'), _textQuestion('2'), _textQuestion('3'), _textQuestion('4')]
    ..subject = 'Subject 1'
    ..topic = 'Topic 1'
    ..library = 'PUBLIC'
    ..userId = '9999';
      
    data[newId()] = new QuestionBlock()
    ..id = lastId() 
    ..title = 'QuestionBlock 2' 
    ..introduction = 'QuestionBlock Intro' 
    ..questions = [_textQuestion('1'), _textQuestion('2'), _textQuestion('3'), _textQuestion('4')]
    ..subject = 'Subject 1'
    ..topic = 'Topic 1'
    ..library = 'PUBLIC'
    ..userId = '9999';
      
    data[newId()] = new QuestionBlock()
    ..id = lastId() 
    ..title = 'QuestionBlock 3' 
    ..introduction = 'QuestionBlock Intro' 
    ..questions = [_textQuestion('1'), _textQuestion('2'), _textQuestion('3'), _textQuestion('4')]
    ..subject = 'Subject 1'
    ..topic = 'Topic 1'
    ..library = 'PUBLIC'
    ..userId = '9999';
      
    data[newId()] = new QuestionBlock()
    ..id = lastId() 
    ..title = 'QuestionBlock 4' 
    ..introduction = 'QuestionBlock Intro' 
    ..questions = [_textQuestion('1'), _textQuestion('2'), _textQuestion('3')]
    ..subject = 'Subject 1'
    ..topic = 'Topic 2'
    ..library = 'PUBLIC'
    ..userId = '9999';
      
    data[newId()] = new QuestionBlock()
    ..id = lastId() 
    ..title = 'QuestionBlock 5' 
    ..introduction = 'QuestionBlock Intro' 
    ..questions = [_textQuestion('1'), _textQuestion('2'), _textQuestion('3')]
    ..subject = 'Subject 1'
    ..topic = 'Topic 2'
    ..library = 'PUBLIC'
    ..userId = '9999';
  
    data[newId()] = new QuestionBlock()
    ..id = lastId() 
    ..title = 'QuestionBlock 6' 
    ..introduction = 'QuestionBlock Intro' 
    ..questions = [_textQuestion('1'), _textQuestion('2'), _textQuestion('3')]
    ..subject = 'Subject 2'
    ..topic = 'Topic 1'
    ..library = 'PUBLIC'
    ..userId = '9999';
    
    data[newId()] = new QuestionBlock()
    ..id = lastId() 
    ..title = 'QuestionBlock 7' 
    ..introduction = 'QuestionBlock Intro' 
    ..questions = [_textQuestion('1'), _textQuestion('2')]
    ..subject = 'Subject 2'
    ..topic = 'Topic 1'
    ..library = 'PUBLIC'
    ..userId = '9999';
    
    data[newId()] = new QuestionBlock()
    ..id = lastId() 
    ..title = 'QuestionBlock 8' 
    ..introduction = 'QuestionBlock Intro' 
    ..questions = [_textQuestion('1'), _textQuestion('2')]
    ..subject = 'Subject 2'
    ..topic = 'Topic 2'
    ..library = 'PUBLIC'
    ..userId = '9999';
    
    data[newId()] = new QuestionBlock()
    ..id = lastId() 
    ..title = 'QuestionBlock 9' 
    ..introduction = 'QuestionBlock Intro' 
    ..questions = [_textQuestion('1'), _textQuestion('2')]
    ..subject = 'Subject 2'
    ..topic = 'Topic 3'
    ..library = 'PUBLIC'
    ..userId = '9999';
  }
  
  /**
   * Helper method to create a [TextQuestion] appending [str].
   */
  static TextQuestion _textQuestion(String str) {
    return new TextQuestion()
    ..text = 'Question Text $str'
    ..points = 3
    ..solution = 'Solution $str'
    ..emptyAnswerLines = 5;
  }
}