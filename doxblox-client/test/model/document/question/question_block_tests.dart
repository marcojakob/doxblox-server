part of model_tests;

const String QUESTION_BLOCK_JSON = '''
{
  "id" : "51267930f5c7f925eaae04d2",
  "title" : "Inventory 1",
  "introduction" : "Introduction to Inventory 1",
  "questions" : [ {
    "emptyAnswerLines" : 5,
    "solution" : "Solution a",
    "text" : "Question Text a",
    "type" : "TextQuestion",
    "points" : 3
  }, {
    "emptyAnswerLines" : 5,
    "solution" : "Solution b",
    "text" : "Question Text b",
    "type" : "TextQuestion",
    "points" : 3
  }, {
    "emptyAnswerLines" : 5,
    "solution" : "Solution c",
    "text" : "Question Text c",
    "type" : "TextQuestion",
    "points" : 3
  }, {
    "emptyAnswerLines" : 5,
    "solution" : "Solution d",
    "text" : "Question Text d",
    "type" : "TextQuestion",
    "points" : 3
  } ],
  "subject" : "Accounting",
  "topic" : "Balance",
  "library" : "PRIVATE",
  "userId" : "111111111111111111111111"
}
''';

/**
 * Tests for the [QuestionBlock] model object. Includes test for [Question]s
 * and its subclasses.
 */
questionBlockTests() {
  group('QuestionBlock Tests:', () {
  
  test('constructorFromJson_AllAttributesSet_ReturnsQuestionBlock', () {
    // when
    QuestionBlock block = new QuestionBlock.fromJson(JSON.decode(QUESTION_BLOCK_JSON));
    
    // then
    expect(block.id, equals('51267930f5c7f925eaae04d2'));
    expect(block.title, equals('Inventory 1'));
    expect(block.introduction, equals('Introduction to Inventory 1'));
    expect(block.subject, equals('Accounting'));
    expect(block.topic, equals('Balance'));
    expect(block.library, equals('PRIVATE'));
    expect(block.userId, equals('111111111111111111111111'));
    
    expect(block.questions, hasLength(4));
    expect(block.questions[0], new isInstanceOf<TextQuestion>('TextQuestion'));
    expect(block.questions[1], new isInstanceOf<TextQuestion>('TextQuestion'));
    expect(block.questions[2], new isInstanceOf<TextQuestion>('TextQuestion'));
    expect(block.questions[3], new isInstanceOf<TextQuestion>('TextQuestion'));
    
    TextQuestion q0 = block.questions[0];
    expect(q0.emptyAnswerLines, equals(5));
    expect(q0.solution, equals('Solution a'));
    expect(q0.text, equals('Question Text a'));
    expect(q0.points, equals(3));
  });
  
  test('constructorFromJson_EmptyJsonString_ReturnsWithNullOrEmptyValues', () {
    // when
    QuestionBlock block = new QuestionBlock.fromJson(JSON.decode('{}'));
    
    // then
    expect(block.id, isNull);
    expect(block.questions, isEmpty);
  });
  
  test('toJsonViaStringify_AllAttributesSet_ReturnsJsonString', () {
    // given
    QuestionBlock block = new QuestionBlock()
    ..id = '1111'
    ..title = 'QuestionBlock 9' 
    ..introduction = 'QuestionBlock Intro' 
    ..questions = [
                   new TextQuestion()
                  ..text = 'Question Text 1'
                  ..points = 9
                  ..solution = 'Solution 1'
                  ..emptyAnswerLines = 3,
                  new TextQuestion()
                   ..text = 'Question Text 2'
                   ..points = 6
                   ..solution = 'Solution 2'
                   ..emptyAnswerLines = 10
                  ]
    ..subject = 'Subject 2'
    ..topic = 'Topic 3'
    ..library = 'PUBLIC'
    ..userId = '9999';
    
    // when
    String jsonString = JSON.encode(block);
    
    // then
    QuestionBlock reconvertedBlock = new QuestionBlock.fromJson(JSON.decode(jsonString));
    expect(reconvertedBlock.id, equals('1111'));
    expect(reconvertedBlock.questions, hasLength(2));
    expect(reconvertedBlock.questions[0], new isInstanceOf<TextQuestion>('TextQuestion'));
    TextQuestion q0 = reconvertedBlock.questions[0];
    expect(q0.emptyAnswerLines, equals(3));
    expect(q0.solution, equals('Solution 1'));
    expect(q0.text, equals('Question Text 1'));
    expect(q0.points, equals(9));
  });
  
  test('toString', () {
    // given
    QuestionBlock block = new QuestionBlock()
    ..id = '1111'
    ..title = 'QuestionBlock 9' 
    ..introduction = 'QuestionBlock Intro' 
    ..questions = [
                   new TextQuestion()
                  ..text = 'Question Text 1'
                  ..points = 3
                  ..solution = 'Solution 1'
                  ..emptyAnswerLines = 5,
                  new TextQuestion()
                   ..text = 'Question Text 2'
                   ..points = 6
                   ..solution = 'Solution 2'
                   ..emptyAnswerLines = 10
                  ]
    ..subject = 'Subject 2'
    ..topic = 'Topic 3'
    ..library = 'PUBLIC'
    ..userId = '9999';
    
    // when
    String str = block.toString();
    
    // then
    expect(str, equals('{"id":"1111","title":"QuestionBlock 9","introduction":"QuestionBlock Intro","questions":[{"text":"Question Text 1","points":3,"emptyAnswerLines":5,"solution":"Solution 1","type":"TextQuestion"},{"text":"Question Text 2","points":6,"emptyAnswerLines":10,"solution":"Solution 2","type":"TextQuestion"}],"subject":"Subject 2","topic":"Topic 3","library":"PUBLIC","userId":"9999"}'));
  });
  });
}

