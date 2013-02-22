package ch.documakery.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bson.types.ObjectId;

import ch.documakery.domain.document.Document;
import ch.documakery.domain.document.DocumentFolder;
import ch.documakery.domain.document.QuestionBlock;
import ch.documakery.domain.document.QuestionBlock.LibraryType;
import ch.documakery.domain.document.question.TextAnswerQuestion;

/**
 * Provides factory methods to generate test data.
 * 
 * @author Marco Jakob
 */
public class TestDataFactory {
  
  /**
   * Adds a whole "Rechnungswesen" tree to the root and adds the objects to the result list.
   * 
   * @param rootFolder
   * @param result a list of result objects that can be persisted.
   */
  public static void createFolderTreeRw(DocumentFolder rootFolder, ObjectId userId, List<Object> result) {
    DocumentFolder folder1 = createFolder("Rechnungswesen", userId, result);
    folder1.setParentId(rootFolder.getId());
    
      DocumentFolder folder11 = createFolder("Einführung RW", userId, result);
      folder1.getDocumentIds().add(folder11.getId());
    
      DocumentFolder folder12 = createFolder("Inventar und Bilanz", userId, result);
      folder12.setParentId(folder1.getId());
      
        DocumentFolder folder121 = createFolder("Archivierte", userId, result);
        folder121.setParentId(folder12.getId());
        
        Document doc121 = createDocument("Prüfung - Inventar, Bilanz, Bilanzveränderung", userId, result);
        folder12.getDocumentIds().add(doc121.getId());
          doc121.getDocumentBlockIds().add(createQuestionBlockDummy("Inventar 1", LibraryType.PRIVATE, userId, result).getId());
          doc121.getDocumentBlockIds().add(createQuestionBlockDummy("Inventar 2", LibraryType.PUBLIC, userId, result).getId());
          doc121.getDocumentBlockIds().add(createQuestionBlockDummy("Inventar 3", LibraryType.PRIVATE, userId, result).getId());
          doc121.getDocumentBlockIds().add(createQuestionBlockDummy("Inventar 4", LibraryType.PRIVATE, userId, result).getId());
          doc121.getDocumentBlockIds().add(createQuestionBlockBwlPrivate1(userId, result).getId());
        
        Document doc122 = createDocument("Prüfung - Konto, Journal, Hauptbuch", userId, result);
        folder12.getDocumentIds().add(doc122.getId());
          
        
      DocumentFolder folder13 = createFolder("Erfolgsrechnung", userId, result);
      folder13.setParentId(folder1.getId());
      
        Document doc131 = createDocument("Prüfung - Erfolgsrechnung", userId, result);
        folder13.getDocumentIds().add(doc131.getId());
          doc131.getDocumentBlockIds().addAll(createQuestionBlockDummies(4, LibraryType.PUBLIC, userId, result));
          
        Document doc132 = createDocument("Prüfung - Erfolgsrechnung", userId, result);
          folder13.getDocumentIds().add(doc132.getId());
          doc132.getDocumentBlockIds().addAll(createQuestionBlockDummies(2, LibraryType.PUBLIC, userId, result));

          
      DocumentFolder folder14 = createFolder("Fremde Währungen", userId, result);
      folder14.setParentId(folder1.getId());
      
        Document doc141 = createDocument("Prüfung - Fremde Währungen", userId, result);
        folder14.getDocumentIds().add(doc141.getId());
        doc141.getDocumentBlockIds().addAll(createQuestionBlockDummies(8, LibraryType.PRIVATE, userId, result));
      
        
      DocumentFolder folder15 = createFolder("Abschluss Einzelunternehmung", userId, result);
      folder15.setParentId(folder1.getId());
      
        Document doc151 = createDocument("Prüfung - Abschluss Einzelunternehmung", userId, result);
        folder15.getDocumentIds().add(doc151.getId());
        doc151.getDocumentBlockIds().addAll(createQuestionBlockDummies(2, LibraryType.PUBLIC, userId, result));
        
        Document doc152 = createDocument("Prüfung - Abschluss Einzelunternehmung (Nachprüfung)", userId, result);
        folder15.getDocumentIds().add(doc152.getId());
        doc152.getDocumentBlockIds().addAll(createQuestionBlockDummies(8, LibraryType.PUBLIC, userId, result));
            
        
      DocumentFolder folder16 = createFolder("Kalkulation im Handel", userId, result);
      folder16.setParentId(folder1.getId());
      
        Document doc161 = createDocument("Prüfung - Kalkulation im Handel", userId, result);
        folder16.getDocumentIds().add(doc161.getId());
        doc161.getDocumentBlockIds().addAll(createQuestionBlockDummies(2, LibraryType.PRIVATE, userId, result));
  }
  
  /**
   * Adds a whole "BWL" tree to the root and adds the objects to the result list.
   * 
   * @param rootFolder
   */
  public static void createFolderTreeBwl(DocumentFolder rootFolder, ObjectId userId, List<Object> result) {
    DocumentFolder folder1 = createFolder("Betriebswirtschaftslehre", userId, result);
    folder1.setParentId(rootFolder.getId());
    
      DocumentFolder folder11 = createFolder("Unternehmung allgemein", userId, result);
      folder1.getDocumentIds().add(folder11.getId());
    
      DocumentFolder folder12 = createFolder("Organisation", userId, result);
      folder12.setParentId(folder1.getId());
      
      DocumentFolder folder13 = createFolder("Marketing", userId, result);
      folder13.setParentId(folder1.getId());
  }
  
  /**
   * Adds a whole "Recht" tree to the root and adds the objects to the result list.
   * 
   * @param rootFolder
   */
  public static void createFolderTreeRecht(DocumentFolder rootFolder, ObjectId userId, List<Object> result) {
    DocumentFolder folder1 = createFolder("Recht", userId, result);
    folder1.setParentId(rootFolder.getId());
    
      DocumentFolder folder11 = createFolder("Recht allgemein", userId, result);
      folder1.getDocumentIds().add(folder11.getId());
  }
  
  /**
   * Creates a folder and adds it to the result list.
   * An id is automatically generated.
   */
  public static DocumentFolder createFolder(String name, ObjectId userId, List<Object> result) {
    DocumentFolder folder = new DocumentFolder(name);
    folder.setId(new ObjectId());
    folder.setUserId(userId);
    result.add(folder);
    return folder;
  }

  /**
   * Creates a document and adds it to the result list.
   * An id is automatically generated.
   */
  public static Document createDocument(String name, ObjectId userId, List<Object> result) {
    Document doc = new Document();
    doc.setName(name);
    doc.setUserId(userId);
    doc.setId(new ObjectId());
    result.add(doc);
    return doc;
  }

  /**
   * Creates a dummy question block. The number of questions inside the blocks
   * varies between 1 and 8.
   * A new id is generated and the QuestionBlock is added to the result list.
   */
  public static QuestionBlock createQuestionBlockDummy(String title, LibraryType libraryType, 
      ObjectId userId, List<Object> result) {
    QuestionBlock block = new QuestionBlock();
    block.setId(new ObjectId());
    block.setTitle(title);
    block.setLibraryType(libraryType);
    block.setIntroduction("Introduction for " + title);
    block.setUserId(userId);

    for (int i = 0; i < (new Random().nextInt(8) + 1); i++) {
      char letter = (char) ('a' + i);
      TextAnswerQuestion q = new TextAnswerQuestion();
      q.setText("Question Text " + letter);
      q.setSolution("Solution " + letter);
      q.setEmptyAnswerLines(5);
      q.setPoints(3);
      q.setCorrectionNotes("Correction Notes " + letter);
      block.getQuestions().add(q);
    }
    
    result.add(block);
    return block;
  }
  
  /**
   * Creates a number dummy question block. The number of questions inside the blocks
   * varies between 1 and 8.
   * A new id is generated for each question block and the question block is added to the 
   * result list.
   * 
   * @param number How many question blocks 
   */
  public static List<ObjectId> createQuestionBlockDummies(int number, LibraryType libraryType, 
      ObjectId userId, List<Object> result) {
    List<ObjectId> blockIds = new ArrayList<ObjectId>();
    
    for (int i = 0; i < number; i++) {
      blockIds.add(createQuestionBlockDummy("Dummy Title " + Integer.toString(i + 1), libraryType,
          userId, result).getId());
    }
    
    return blockIds;
  }

  /**
   * Creates a new "BWL" question block.
   * A new id is generated and the QuestionBlock is added to the result list.
   */
  public static QuestionBlock createQuestionBlockBwlPrivate1(ObjectId userId, List<Object> result) {
    QuestionBlock block = new QuestionBlock();
    block.setId(new ObjectId());
    block.setTitle("Leitbild des Hotels/Restaurants Edelweiss");
    block.setLibraryType(LibraryType.PRIVATE);
    block.setIntroduction("Die neue Betriebsassistentin Selina Poltera möchte durch die Formulierung " +
        "eines Leitbilds ein paar grundlegende Ziele des Hotels/Restaurants Edelweiss festhalten. " +
        "Beantworten Sie zu diesem Leitbild folgende Fragen:");
    block.setUserId(userId);
    
    TextAnswerQuestion q1 = new TextAnswerQuestion();
    q1.setText("In einem Leitbild werden unter anderem die anzustrebenden Beziehungen zu den " +
        "Anspruchsgruppen beschrieben. Welche wichtige Anspruchsgruppe fehlt im vorliegenden " +
        "Entwurf des Leitbildes?");
    q1.setSolution("Mitarbeitende");
    q1.setEmptyAnswerLines(1);
    q1.setPoints(1);
    q1.setCorrectionNotes("Anspruchsgruppe Konkurrenz auch gelten lassen");
    block.getQuestions().add(q1);
    
    TextAnswerQuestion q = new TextAnswerQuestion();
    q.setText("Welche Zielformulierung des Entwurfes gehört nicht in ein Leitbild. Zitieren Sie die " +
        "entsprechende Stelle aus dem Leitbild und begründen Sie Ihre Wahl.");
    q.setSolution("\"Um die geplanteRenovierung der Gästezimmer zu finanzieren, werden wir eine " +
        "Aktienkapitalerhöhung von CHF 400'000.- durchführen. Die Restfinanzierung dieser Investition " +
        "werden wir mit der Graubündner Kantonalbank besprechen.\"");
    q.setEmptyAnswerLines(7);
    q.setPoints(2);
    q.setCorrectionNotes("");
    block.getQuestions().add(q);
    
    result.add(block);
    return block;
  }
}
